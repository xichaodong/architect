package com.tristeza.distributiontransaction.xa.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author chaodong.xi
 * @date 2020/8/1 6:56 下午
 */
@Configuration
@MapperScan(value = "com.tristeza.distributiontransaction.xa.mapper.db71", sqlSessionFactoryRef = "sqlSessionFactory71")
public class DB71DataSource {

    @Bean("db71")
    public DataSource db71() {
        MysqlXADataSource dataSource = new MysqlXADataSource();
        dataSource.setUser("root");
        dataSource.setPassword("abcABC@123");
        dataSource.setUrl("jdbc:mysql://172.16.16.71:3306/xa_71?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=GMT%2B8");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(dataSource);

        return atomikosDataSourceBean;
    }

    @Bean("sqlSessionFactory71")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("db71") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("mapper/db71/*.xml"));

        return sqlSessionFactoryBean;
    }
}
