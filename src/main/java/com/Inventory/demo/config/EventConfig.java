package com.Inventory.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    // Queue names
    public static final String STOCK_MOVEMENT_QUEUE = "stock-movement-queue";
    public static final String STOCK_UPDATE_QUEUE = "stock-update-queue";

    // Exchange names
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";

    // Routing keys
    public static final String STOCK_MOVEMENT_ROUTING_KEY = "stock.movement";
    public static final String STOCK_UPDATE_ROUTING_KEY = "stock.update";

    @Bean
    public Queue stockMovementQueue() {
        return new Queue(STOCK_MOVEMENT_QUEUE, true);
    }

    @Bean
    public Queue stockUpdateQueue() {
        return new Queue(STOCK_UPDATE_QUEUE, true);
    }

    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding stockMovementBinding(Queue stockMovementQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(stockMovementQueue).to(inventoryExchange).with(STOCK_MOVEMENT_ROUTING_KEY);
    }

    @Bean
    public Binding stockUpdateBinding(Queue stockUpdateQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(stockUpdateQueue).to(inventoryExchange).with(STOCK_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}