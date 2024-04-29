package com.murro.inv.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import(DatabaseConfig.class)
public class OrmConfig {
    @Bean("dataSource")
    DataSource dataSource(DatabaseConfig config){
        HikariConfig dataSourceProps = new HikariConfig();
        System.out.println(config.getJdbcURL());
        dataSourceProps.setJdbcUrl(config.getJdbcURL());
        dataSourceProps.setUsername(config.getUser());
        dataSourceProps.setPassword(config.getPassword());
        return new HikariDataSource(dataSourceProps);
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            DatabaseConfig databaseConfig
    ){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(false);
        adapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setPackagesToScan(databaseConfig.getPackages());
        factory.setDataSource(dataSource);
        return factory;
    }

    @Bean("transactionManager")
    protected PlatformTransactionManager transactionManager
            (EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
