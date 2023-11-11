package org.exchanger_bot.config;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.queues.verifying-email}")
    private String registrationMailQueue;

    @Value("${spring.rabbitmq.queues.verifying-email-answer}")
    private String registrationMailAnswerQueue;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(registrationMailQueue);
    }

    @Bean
    public Queue textMessageAnswerQueue() {
        return new Queue(registrationMailAnswerQueue);
    }

}
