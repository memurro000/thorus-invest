package com.murro.inv.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import(DatabaseConfig.class)
public class OrmConfig {
    
    private String resolve(String property){
        if(property.charAt(0) == '$'){
            return System
                    .getenv(
                            property.substring(2, property.length() - 1
                            ));
        }
        return property;
    }
    
    @Bean("dataSource")
    DataSource dataSource(DatabaseConfig databaseConfig){
        HikariConfig dataSourceProps = new HikariConfig();
        dataSourceProps.setJdbcUrl(resolve(databaseConfig.getJdbcURL()));
        dataSourceProps.setUsername(resolve(databaseConfig.getUser()));
        dataSourceProps.setPassword(resolve(databaseConfig.getPassword()));
        return new HikariDataSource(dataSourceProps);
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            DatabaseConfig databaseConfig
    ){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        /* adapter.setDatabase(Database.POSTGRESQL); */
        /* IS NOT NEEDED TILL DBMS IS CHANGED */
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
