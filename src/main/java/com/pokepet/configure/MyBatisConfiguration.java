package com.pokepet.configure;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Fade on 2016/12/22.
 */



@Configuration
@Order(42)
public class MyBatisConfiguration{


   private static final Logger log= LoggerFactory.getLogger(MyBatisConfiguration.class);

   @Autowired
   @Qualifier(value = "datasource")
   private DataSource datasource;


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory initSqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean= new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.zy.model");



        log.info("initialize SqlSessionFactoryBean  ..............");
        Properties properties=new Properties();
//        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "false");
        properties.setProperty("returnPageInfo", "check");
        PageInterceptor pageInterceptor=new PageInterceptor();
        pageInterceptor.setProperties(properties);

        //add pageHelpPlugin
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});

        //PathResource
        ResourcePatternResolver resourceResolver=new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourceResolver.getResources("classpath*:mapper/*.xml"));
        return  sqlSessionFactoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
           return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(datasource);
        return dataSourceTransactionManager;
    }




}
