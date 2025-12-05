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

    private static final String PRODUCT_EXCHANGE_NAME = "product-exchange";
    private static final String ORDER_EXCHANGE_NAME = "order-exchange";
    private static final String CREATE_ROUTING_KEY = "product.created";
    private static final String DELETE_ROUTING_KEY = "product.deleted";
    private static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
    private static final String PRODUCT_QUEUE_NAME = "product-queue";
    private static final String INVENTORY_ORDER_QUEUE = "inventory-order-queue";


    /**
     * Declare a TopicExchange named product-exchange
     *
     */
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE_NAME);
    }


    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME);
    }

    /**
     * Declare a durable queues
     *
     */
    @Bean
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE_NAME, true);
    }

    @Bean
    public Queue inventoryOrderQueue() {
        return new Queue(INVENTORY_ORDER_QUEUE, true);
    }

    /**
     * Bind the product-queue to 'product-exchange' with routing keys
     *
     */
    @Bean
    public Binding bindingCreate(Queue productQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with(CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDelete(Queue productQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with(DELETE_ROUTING_KEY);
    }

    /**
     * Bind the order-queue to 'order-exchange' with routing key 'order.placed'
     *
     */
    @Bean
    public Binding bindingOrder(Queue inventoryOrderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(inventoryOrderQueue).to(orderExchange).with(ORDER_PLACED_ROUTING_KEY);
    }

    /**
     * JSON converter for RabbitMQ messages
     *
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
