package com.pokepet.configure;

import java.beans.PropertyVetoException;

import javax.naming.NamingException;
import javax.sql.DataSource;

//import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Created by Fade on 2016/12/22.
 */

@Configuration
@Order(1)
public class MyBaitsAutoMappingConfiguration {


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.pokepet.dao");
        return mapperScannerConfigurer;
    }


    @Bean(value = "datasource")
    public DataSource dataSource() throws NamingException, PropertyVetoException {
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://116.62.60.203:3306/pokedata?characterEncoding=utf8&useSSL=false&autoReconnect=true");
        dataSource.setUser("root");
        dataSource.setPassword("PokePet123456!");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setTestConnectionOnCheckin(false);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setMaxIdleTime(600000);
        dataSource.setMaxPoolSize(100);
        dataSource.setAutoCommitOnClose(false);
        return dataSource;
    }
    
    /*@Bean(value = "datasource")
    public DataSource dataSource(){
        HikariDataSource hikariDataSource=new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/pokedata?serverTimezone=UTC&characterEncoding=utf8&useSSL=false");
        hikariDataSource.setUsername("bean");
        hikariDataSource.setPassword("bean1234");
        hikariDataSource.setReadOnly(false);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(600000);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setMaximumPoolSize(12);
        hikariDataSource.setMaxLifetime(1800000);
        return hikariDataSource;
    }*/



}
