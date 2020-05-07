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
package org.apache.guacamole.springconfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DatasourceConfig {

    private Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);

    /**
     * 配置DataSource
     *
     * @return
     * @throws SQLException
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource dataSource() {
        String username = "guacamole_user";
        String password = "guacamole_password";
        String mysqlhostname = "localhost";
        String mysqldatabase = "guacamole_db";
        String timeZone = "GMT%2B8";
        String guacaHome = System.getenv("GUACAMOLE_HOME");
        Properties properties = readProperties(guacaHome + File.separator + "guacamole.properties");
        if (properties != null) {
            username = (properties.getProperty("mysql-username") == null ? username : properties.getProperty("mysql-username"));
            password = (properties.getProperty("mysql-password") == null ? password : properties.getProperty("mysql-password"));
            mysqlhostname = (properties.getProperty("mysql-hostname") == null ? mysqlhostname : properties.getProperty("mysql-hostname"));
            logger.info("guacaHome properties.username:{},password:{},hostname:{}", username, password, mysqlhostname);
        }
        String url = properties.getProperty("mysql-url");
        logger.info("Datasource config url:{}",url);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setUrl(url);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setUseGlobalDataSourceStat(true);
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DruidDataSource druidDataSource) {
        return new JdbcTemplate(druidDataSource);
    }


    private Properties readProperties(String path) {
        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new FileReader(path));
            properties.load(bufferedReader);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
