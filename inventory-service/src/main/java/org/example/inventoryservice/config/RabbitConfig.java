package org.example.inventoryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String EXCHANGE_NAME = "product-exchange";
    private static final String CREATE_ROUTING_KEY = "product.created";
    private static final String DELETE_ROUTING_KEY = "product.deleted";
    private static final String QUEUE_NAME = "inventory-queue";

    /**
     * Declare a TopicExchange named product-exchange
     * */
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Declare a durable queue
     * */
    @Bean
    public Queue inventoryQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Bind the inventory queue to 'product-exchange' with routing key 'product.created'
     * */
    @Bean
    public Binding bindingCreate(Queue inventoryQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(inventoryQueue).to(productExchange).with(CREATE_ROUTING_KEY);
    }
    /**
     * Bind the inventory queue to 'product-exchange' with routing key 'product.deleted'
     * */
    @Bean
    public Binding bindingDelete(Queue inventoryQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(inventoryQueue).to(productExchange).with(DELETE_ROUTING_KEY);
    }

    /**
     * JSON converter for RabbitMQ messages
     * */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
