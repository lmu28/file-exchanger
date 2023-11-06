package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.RowData;
import org.exchanger_bot.repository.RowDataRepository;
import org.exchanger_bot.service.ConsumerService;
import org.exchanger_bot.service.MainService;
import org.exchanger_bot.service.ProducerService;
import org.exchanger_bot.service.RowDataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.exchanger_bot.model.RabbitQueue.*;


@Service
@Log4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

   private final MainService mainService;



    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
//        log.info("NODE: text message is received");
        mainService.processTextMassage(update);


//        Message message = update.getMessage();
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(update.getMessage().getChatId());
//        sendMessage.setText("hello from node");
//        producerService.produceAnswer(sendMessage);




    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdate(Update update) {
        log.info("NODE: doc message is received");
        mainService.processDocumentMassage(update);

    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdate(Update update) {
        log.info("NODE: photo message is received");
        mainService.processPhotoMassage(update);

    }
}
