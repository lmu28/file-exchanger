package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.MailProducer;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Log4j
@RequiredArgsConstructor
@Service
public class MailProducerImpl implements MailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produce(String rabbitQueue, MailActivationResp response) {
        rabbitTemplate.convertAndSend(rabbitQueue,response);
    }
}
