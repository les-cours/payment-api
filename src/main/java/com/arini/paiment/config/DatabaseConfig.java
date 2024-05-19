package com.arini.paiment.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@Component
public class DatabaseConfig {

    @Value("${app_postgres_dbName}")
    private String databaseName;

    @Value("${app_postgres_username}")
    private String username;

    @Value("${app_postgres_password}")
    private String password;

    @Value("${app_postgres_host}")
    private String host;

    @Value("${app_postgres_port}")
    private String port;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://"+host+":"+port+"/"+databaseName+"?sslmode=disable");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
