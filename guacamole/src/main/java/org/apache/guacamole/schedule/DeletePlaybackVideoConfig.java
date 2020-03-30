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

import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.net.SSLGuacamoleSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;


@Component
@EnableScheduling
public class DeletePlaybackVideoConfig implements SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(DeletePlaybackVideoConfig.class);

    @Autowired
    private FileUtilService fileUtilService;

    //Execute every day at 2 am by default
    private String cron = "0 0 2 * * ?";

    public void setCron(String cron) {
        this.cron = cron;
    }

    @PostConstruct
    public void registerTaskHandler() {
        String guacamoleHome = System.getenv("GUACAMOLE_HOME");
        String retentionSize = System.getenv("ENV_PLAYBACK_VIDEO_MAX_RETENTION_SIZE");
        String cronStr = System.getenv("ENV_DELETE_PLAYBACK_SCHEDULE_CONFIG");
        logger.info("Delete playback video scheduled Config.guacamoleHome:{}.retentionSize:{}.cron:{}",guacamoleHome,retentionSize,cronStr);
        if(StringUtils.isNotBlank(cronStr)){
            this.setCron(cronStr);
        }
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                //Keep files by default for 180 days
                BigDecimal retentionDay = new BigDecimal(180);
                //Default save path
                String dirPath = "/opt/manager/resources/guacd";
                //Keep files by default for Maximum size 10 GB
                BigDecimal maxRetentionSize = new BigDecimal(10 * 1024);
                String dirPathStr = System.getenv("ENV_GUACAMOLE_RECORD_PATH");
                String maxRetentionDayStr = System.getenv("ENV_PLAYBACK_VIDEO_MAX_RETENTION_DAY");
                String maxRetentionSizeStr = System.getenv("ENV_PLAYBACK_VIDEO_MAX_RETENTION_SIZE");
                if (StringUtils.isNotBlank(dirPathStr)) {
                    dirPath = dirPathStr;
                }
                if (StringUtils.isNotBlank(maxRetentionDayStr)) {
                    retentionDay = new BigDecimal(maxRetentionDayStr);
                }
                if (StringUtils.isNotBlank(maxRetentionSizeStr)) {
                    maxRetentionSize = new BigDecimal(maxRetentionSizeStr);
                }
                logger.info("Delete playback video scheduled task start.dirPath:{}.retentionDay:{}.maxRetentionSize:{}",dirPath,retentionDay,maxRetentionSize);
                try {
                    fileUtilService.removeOverSizeFiles(dirPath, maxRetentionSize);
                    fileUtilService.removeExpireFiles(dirPath, retentionDay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logger.info("Delete playback video scheduled task end.total time：[{}] millisecond", System.currentTimeMillis() - startTime);
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // Timing task trigger, can modify the execution cycle of timing tasks
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }


}