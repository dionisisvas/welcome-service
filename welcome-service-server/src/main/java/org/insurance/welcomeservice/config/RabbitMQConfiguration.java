package org.insurance.welcomeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class regarding receiving messages via AQMP
 */
@Configuration
public class RabbitMQConfiguration {

  @Value("${rabbitmq.queue}")
  private String queueName;

  @Value("${rabbitmq.topic-exchange}")
  private String topicExchangeName;

  @Value("${rabbitmq.routing-key}")
  private String routingKey;

  @Bean
  public Queue policyIssuedQueue() {
    return new Queue(queueName);
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  public Binding policyIssuedBinding(Queue policyIssuedQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(policyIssuedQueue).to(topicExchange).with(routingKey);
  }
}
