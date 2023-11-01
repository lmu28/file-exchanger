package org.exchanger_bot.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.exchanger_bot.model.RabbitQueue.*;

@Configuration
public class RabbitConfig {


    @Bean
    public MessageConverter messageConverter(){
        return  new Jackson2JsonMessageConverter();
    }





}
