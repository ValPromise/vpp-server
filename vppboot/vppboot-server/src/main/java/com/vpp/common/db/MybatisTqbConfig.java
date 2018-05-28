package com.vpp.common.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 多数据源，指定basePackages扫描的包
 * 
 * @author Lxl
 * @version V1.0 2017年12月19日
 */
@Configuration
@MapperScan(basePackages = { "com.vpp" }, sqlSessionFactoryRef = "sqlSessionFactoryTqb")
public class MybatisTqbConfig {

    @Autowired
    @Qualifier("tqbDs")
    private DataSource tqbDs;

    @Bean(name = "sqlSessionFactoryTqb")
    @Primary
    public SqlSessionFactory sqlSessionFactoryTqb() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(tqbDs); // 使用tqb数据源
        return factoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplateTqb")
    @Primary
    public SqlSessionTemplate sqlSessionTemplateTqb() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryTqb()); // 使用上面配置的Factory
        return template;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(tqbDs);
    }
}