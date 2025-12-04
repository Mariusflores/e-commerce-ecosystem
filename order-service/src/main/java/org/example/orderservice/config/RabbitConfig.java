package org.example.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String PAYMENT_EXCHANGE_NAME = "payment-exchange";
    private static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";
    private static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";
    private static final String PAYMENT_COMPLETED_QUEUE_NAME = "payment-completed-queue";
    private static final String PAYMENT_FAILED_QUEUE_NAME = "payment-failed-queue";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE_NAME);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE_NAME, true);
    }

    @Bean
    public Queue paymentFailedQueue() {
        return new Queue(PAYMENT_FAILED_QUEUE_NAME, true);
    }


    @Bean
    public Binding bindingPaymentFailed(Queue paymentFailedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentFailedQueue).to(paymentExchange).with(PAYMENT_FAILED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPaymentCompleted(Queue paymentCompletedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentCompletedQueue).to(paymentExchange).with(PAYMENT_COMPLETED_ROUTING_KEY);
    }


    /**
     * JSON converter for RabbitMQ messages
     **/
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a RabbitTemplate bean configured to use Jackson2JsonMessageConverter
     * for JSON serialization and deserialization of messages.
     **/
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
}
