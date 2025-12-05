package org.example.paymentsservice.config;

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

    private static final String ORDER_EXCHANGE_NAME = "order-exchange";
    private static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
    private static final String PAYMENTS_ORDER_QUEUE = "payment-order-queue";
    private static final String PAYMENTS_EXCHANGE_NAME = "payments-exchange";

    /**
     * Publisher Beans
     *
     */

    @Bean
    public TopicExchange paymentsExchange() {
        return new TopicExchange(PAYMENTS_EXCHANGE_NAME);
    }

    /**
     * Consumer Beans
     *
     */
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME);
    }

    @Bean
    public Queue paymentsOrderQueue() {
        return new Queue(PAYMENTS_ORDER_QUEUE, true);
    }

    @Bean
    public Binding bindingOrder(Queue paymentsOrderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentsOrderQueue).to(orderExchange).with(ORDER_PLACED_ROUTING_KEY);
    }

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
