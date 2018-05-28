package com.vpp.common.db;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 多数据源
 * @author Lxl
 * @version V1.0 2017年12月19日
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "tqbDs")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.vpp")
    // application.properteis中对应属性的前缀
    public DataSource tqbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "weatherDs")
    @ConfigurationProperties(prefix = "spring.datasource.weather")
    // application.properteis中对应属性的前缀
    public DataSource weatherDataSource() {
        return DataSourceBuilder.create().build();
    }

}
