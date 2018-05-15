package com.pokepet.configure;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * Created by Fade on 2016/12/22.
 */

@Configuration
@Order(1)
public class MyBaitsAutoMappingConfiguration {


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.zy.dao");
        return mapperScannerConfigurer;
    }


    @Bean(value = "datasource")
    public DataSource dataSource(){
        HikariDataSource hikariDataSource=new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://116.62.60.203:3306?characterEncoding=utf8&useSSL=false");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("PokePet123456!");
        hikariDataSource.setReadOnly(false);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(600000);
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        hikariDataSource.setMaximumPoolSize(12);
        hikariDataSource.setMaxLifetime(1800000);
        return hikariDataSource;
    }



}
