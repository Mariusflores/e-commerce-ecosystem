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
     * Declare the Exchange
     * */
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Declare the queue
     * */
    @Bean
    public Queue inventoryQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Bind Exchange to inventory-queue with routing keys
     * */
    @Bean
    public Binding bindingCreate(Queue inventoryQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(inventoryQueue).to(productExchange).with(CREATE_ROUTING_KEY);
    }
    @Bean
    public Binding bindingDelete(Queue inventoryQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(inventoryQueue).to(productExchange).with(DELETE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
