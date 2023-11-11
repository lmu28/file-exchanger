package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.ProducerMailService;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Log4j
@RequiredArgsConstructor
@Service
public class ProducerMailServiceImpl implements ProducerMailService {


    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.queues.verifying-email}")
    private String verifyingEmailQueue;


    @Override
    public void produce(String rabbitQueue, UserMailInfo userMailInfo) {
        rabbitTemplate.convertAndSend(rabbitQueue,userMailInfo);
    }
}
