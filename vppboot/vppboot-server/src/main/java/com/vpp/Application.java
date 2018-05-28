package com.vpp;

import org.apache.ibatis.annotations.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//异步支持
@EnableAsync
// springboot主程序，禁用数据库自动加载(配置多数据源)
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan
@ServletComponentScan
@EnableTransactionManagement // 如果mybatis中service实现类中加入事务注解，需要此处添加该注解
@MapperScan(basePackages = "com.vpp.*")
@EnableScheduling // 定时任务
public class Application extends SpringBootServletInitializer {
    // private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final Logger logger = LogManager.getLogger(Application.class);

    @Value("${origin.url}")
    String originUrl;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        String[] services = ctx.getBeanNamesForAnnotation(Service.class);

        logger.info("services注解个数：{}", services.length);
        for (String bn : services) {
            logger.info(":::{}", bn);
        }
        String[] mappers = ctx.getBeanNamesForAnnotation(Mapper.class);
        logger.info("mappers注解个数：{}", mappers.length);
        for (String bn : mappers) {
            logger.info(":::{}", bn);
        }
        // String[] components = ctx.getBeanNamesForAnnotation(Component.class);
        // logger.info("components注解个数：{}", components.length);
        // for (String bn : components) {
        // logger.info(":::{}", bn);
        // }

        String[] restControllers = ctx.getBeanNamesForAnnotation(RestController.class);
        logger.info("restControllers注解个数：{}", restControllers.length);
        for (String bn : restControllers) {
            logger.info(":::{}", bn);
        }
    }

    /**
     * 由外部tomcat启动加载项，自启动可删除
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    // @Bean
    // public EmbeddedServletContainerCustomizer containerCustomizer() {
    //
    // return new EmbeddedServletContainerCustomizer() {
    // @Override
    // public void customize(ConfigurableEmbeddedServletContainer container) {
    //
    // ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/toerror");
    // ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/toerror");
    // ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/toerror");
    //
    // container.addErrorPages(error401Page, error404Page, error500Page);
    // }
    // };
    // }

    /**
     * ----支持跨域----
     * 
     * @author Lxl
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
