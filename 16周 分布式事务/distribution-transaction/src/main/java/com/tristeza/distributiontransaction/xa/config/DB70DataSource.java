package com.tristeza.distributiontransaction.xa.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.IOException;

/**
 * @author chaodong.xi
 * @date 2020/8/1 6:56 下午
 */
@Configuration
@MapperScan(value = "com.tristeza.distributiontransaction.xa.mapper.db70", sqlSessionFactoryRef = "sqlSessionFactory70")
public class DB70DataSource {

    @Bean("db70")
    public DataSource db70() {
        MysqlXADataSource dataSource = new MysqlXADataSource();
        dataSource.setUser("root");
        dataSource.setPassword("abcABC@123");
        dataSource.setUrl("jdbc:mysql://172.16.16.70:3306/xa_70?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=GMT%2B8");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(dataSource);

        return atomikosDataSourceBean;
    }

    @Bean("sqlSessionFactory70")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("db70") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("mapper/db70/*.xml"));

        return sqlSessionFactoryBean;
    }

    @Bean("xaTransaction")
    public JtaTransactionManager jtaTransactionManager() {
        UserTransaction userTransaction = new UserTransactionImp();
        UserTransactionManager userTransactionManager = new UserTransactionManager();

        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
