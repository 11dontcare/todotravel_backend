package org.example.todotravel.global.config.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableMongoRepositories(basePackages = "org.example.todotravel.domain.chat.repository")
public class ChatMongoDBConfig {
    @Bean(name = "reactiveMongoTransactionManager")
    public ReactiveMongoTransactionManager reactiveMongoTransactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveMongoTransactionManager reactiveMongoTransactionManager) {
        return TransactionalOperator.create(reactiveMongoTransactionManager);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory factory, MappingMongoConverter converter) {
        return new ReactiveMongoTemplate(factory, converter);
    }
}
