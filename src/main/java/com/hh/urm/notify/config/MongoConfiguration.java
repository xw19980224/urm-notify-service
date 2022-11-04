package com.hh.urm.notify.config;

import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.WriteConcernResolver;

@Configuration
public class MongoConfiguration {

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory factory) {
        return new MongoTransactionManager(factory);
    }

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> WriteConcern.W2;
    }
}

