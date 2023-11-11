package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.MailConsumer;
import org.exchanger_bot.service.MailService;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Log4j
@RequiredArgsConstructor
@Service
public class MailConsumerImpl implements MailConsumer {

    private final MailService mailService;


    @RabbitListener(queues = "${spring.rabbitmq.queues.verifying-email}")
    @Override
    public void consume(UserMailInfo userMailInfo) {

        mailService.processUserMailInfo(userMailInfo);

    }
}
