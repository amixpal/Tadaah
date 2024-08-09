package com.tadaah.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ components including queues, exchanges, and bindings.
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    private static final String QUEUE_NAME = "notifications.queue";
    private static final String EXCHANGE_NAME = "notifications.exchange";
    private static final String ROUTING_KEY = "notifications.routingkey";

    /**
     * Creates a new queue for notifications.
     *
     * @return the queue instance
     */
    @Bean
    public Queue queue() {
        logger.info("Creating RabbitMQ queue with name: {}", QUEUE_NAME);
        return new Queue(QUEUE_NAME, false); // Non-durable queue
    }

    /**
     * Creates a new direct exchange for notifications.
     *
     * @return the exchange instance
     */
    @Bean
    public DirectExchange exchange() {
        logger.info("Creating RabbitMQ direct exchange with name: {}", EXCHANGE_NAME);
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Binds the queue to the exchange with the specified routing key.
     *
     * @param queue the queue instance
     * @param exchange the exchange instance
     * @return the binding instance
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        logger.info("Creating binding for queue '{}' with exchange '{}' and routing key '{}'",
            queue.getName(), exchange.getName(), ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Configures the message converter for RabbitMQ to use Jackson for JSON serialization.
     *
     * @return the message converter instance
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        logger.info("Configuring Jackson2JsonMessageConverter for RabbitMQ");
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the RabbitTemplate to use the provided ConnectionFactory and message converter.
     *
     * @param connectionFactory the connection factory instance
     * @return the RabbitTemplate instance
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        logger.info("Creating RabbitTemplate with custom ConnectionFactory");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
