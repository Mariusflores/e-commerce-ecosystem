package org.example.productservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuration class to enable MongoDB auditing.
 * This allows automatic population of creation and update timestamps
 * on MongoDB documents.
 * */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
