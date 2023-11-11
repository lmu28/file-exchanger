package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.ConsumerMailService;
import org.exchanger_bot.service.ConsumerService;
import org.exchanger_bot.service.MainService;
import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@Log4j
@RequiredArgsConstructor
public class ConsumerMailServiceImpl implements ConsumerMailService {

   private final UserManipulationService userManipulationService;


    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.verifying-email-answer}")
    public void consume(MailActivationResp mailActivationResp) {
        userManipulationService.processIsActivatedEmail(mailActivationResp);
    }


}
