/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.guacamole.schedule;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class FileUtilService {

    private static Logger logger = LoggerFactory.getLogger(FileUtilService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void updateFileStatus(String fileName) {
        try {
            Map<String, Object> dataMap = jdbcTemplate.queryForMap("select connection_id,exts from  guacamole_connection_history where recording_name= ?", new Object[]{fileName});
            if (dataMap == null || dataMap.get("exts") == null || dataMap.get("exts").equals("")) {
                return;
            }
            String exts = (String) dataMap.get("exts");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> tmpMap = mapper.readValue(exts, Map.class);
            tmpMap.put("recordIsDeleted", true);
            jdbcTemplate.update("update guacamole_connection_history set exts = ? where recording_name= ?", new Object[]{mapper.writeValueAsString(tmpMap), fileName});
        } catch (Exception e) {
            logger.error("save connection record method.error:{}", e.getMessage());
        }
    }

    /**
     * Calculate the total size of the file and determine if it exceeds the size(MB)。
     * If it exceeds size, delete 20% of files that have not been used recently
     *
     * @param dirPath
     * @param size
     * @throws IOException
     */
    public void removeOverSizeFiles(String dirPath, BigDecimal size) throws IOException {
        logger.info("Remove overSize files method start.dirPath:{},size:{}", dirPath, size);
        long startTime = System.currentTimeMillis();
        List<Path> pathList = Files.list(Paths.get(dirPath)).collect(Collectors.toList());
        size = size.multiply(new BigDecimal(1024 * 1024));
        for (Path path : pathList) {
            if (!Files.isDirectory(path)) {
                return;
            }
            //Calculate the total size of the file and determine if it exceeds the size
            File[] files = new File(path.toString()).listFiles();
            if (filesSize(files) < size.intValue()) {
                return;
            }
            //Delete 20% of files that have not been used recently
            int removeFactor = (int) Math.ceil(0.2 * files.length);
            //Sort files by last modified time
            Arrays.sort(files, new FileLastModifiedSort());
            for (int i = 0; i < removeFactor; i++) {
                File tmp = files[i];
                if (!tmp.isFile()) {
                    continue;
                }
                logger.info("Delete overSize file.path:{}", tmp.getAbsolutePath());
                try {
                    tmp.delete();
                    // updateFileStatus(tmp.getName());
                    updateFileStatus("guacamole_video");
                } catch (Exception e) {
                    //If it is a multi-node deletion, it exits with an error
                    logger.info("Delete overSize file error.path:{}.error:{}", tmp.getAbsolutePath(), e.getMessage());
                }
            }
        }
        logger.info("Remove overSize files method end.total time：[{}] millisecond", System.currentTimeMillis() - startTime);
    }

    /**
     * Calculate the total size of a file
     *
     * @param files
     * @return
     */
    public static int filesSize(File[] files) {
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            File tmp = files[i];
            if (tmp.isFile()) {
                dirSize += tmp.length();
            }
        }
        return dirSize;
    }

    /**
     * Sort files by time, sort based on the file's last modification time
     * Sort in ascending order of last modification time
     */
    static class FileLastModifiedSort implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            if (f1.lastModified() > f2.lastModified()) {
                return 1;
            } else if (f1.lastModified() == f2.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * Delete files older than the specified number of days
     *
     * @param dirPath
     * @param day
     */
    public void removeExpireFiles(String dirPath, BigDecimal day) {
        logger.info("Remove expire files start.dirPath:{},day:{}", dirPath, day);
        day = day.multiply(new BigDecimal(24 * 60 * 60 * 1000));
        long startTime = System.currentTimeMillis();
        try {
            long cutOff = System.currentTimeMillis() - day.longValue();
            List<Path> pathList = Files.list(Paths.get(dirPath)).collect(Collectors.toList());
            for (Path path : pathList) {
                if (Files.isDirectory(path)) {
                    removeExpireFiles(path.toString(), day);
                } else {
                    try {
                        long modifiedTime = Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
                        if (modifiedTime < cutOff) {
                            logger.info("Delete expire file.path:{}", path);
                            Files.delete(path);
                        }
                    } catch (Exception e) {
                        //If it is a multi-node deletion, it exits with an error
                        logger.info("Delete expire file path:{}. error:{}", path, e.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Remove expire files error:{}", ex);
        }
        logger.info("Remove expire files end,total time：[{}] millisecond", System.currentTimeMillis() - startTime);
    }
}
