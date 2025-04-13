//package com.Inventory.demo.config;
//
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableTransactionManagement
//public class DataSourceConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.write")
//    public DataSource writeDataSource() {
//        return DataSourceBuilder.create()
//                .type(com.zaxxer.hikari.HikariDataSource.class)
//                .build();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.read")
//    public DataSource readDataSource() {
//        return DataSourceBuilder.create()
//                .type(com.zaxxer.hikari.HikariDataSource.class)
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public DataSource routingDataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
//                                        @Qualifier("readDataSource") DataSource readDataSource) {
//        RoutingDataSource routingDataSource = new RoutingDataSource();
//
//        Map<Object, Object> dataSources = new HashMap<>();
//        dataSources.put("write", writeDataSource);
//        dataSources.put("read", readDataSource);
//
//        routingDataSource.setTargetDataSources(dataSources);
//        routingDataSource.setDefaultTargetDataSource(writeDataSource);
//
//        return routingDataSource;
//    }
//
//    public static class RoutingDataSource extends AbstractRoutingDataSource {
//        @Override
//        protected Object determineCurrentLookupKey() {
//            return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "read" : "write";
//        }
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder, @Qualifier("routingDataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.Inventory.demo.model")
//                .persistenceUnit("default")
//                .build();
//    }
//}

package com.Inventory.demo.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
                                        @Qualifier("readDataSource") DataSource readDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("write", writeDataSource);
        dataSources.put("read", readDataSource);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource);

        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("routingDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.Inventory.demo") // This will scan all packages under com.Inventory.demo
                .persistenceUnit("default")
                .build();
    }

    public static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "read" : "write";
        }
    }
}