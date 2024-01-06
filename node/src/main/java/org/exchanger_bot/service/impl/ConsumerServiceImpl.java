package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.ConsumerService;
import org.exchanger_bot.service.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.exchanger_bot.model.RabbitQueue.*;



@Service
@Log4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

   private final MainService mainService;


    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
    public void consumeTextMessageUpdate(Update update) {
        mainService.processTextMassage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
    public void consumeDocMessageUpdate(Update update) {
//        log.info("NODE: doc message is received");
        mainService.processDocumentMassage(update);

    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
    public void consumePhotoMessageUpdate(Update update) {
//        log.info("NODE: photo message is received");
        mainService.processPhotoMassage(update);

    }
}
