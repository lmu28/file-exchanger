package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.controller.UpdateController;
import org.exchanger_bot.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.exchanger_bot.model.RabbitQueue.ANSWER_MESSAGE;
import static org.exchanger_bot.model.RabbitQueue.TEXT_MESSAGE_UPDATE;


@Service
@RequiredArgsConstructor
@Log4j
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateController updateController;

    @RabbitListener(queues = ANSWER_MESSAGE)
    @Override
    public void consume(SendMessage sendMessage) {
        log.info("Dispatcher: Answer message is received");
        updateController.setView(sendMessage);

    }








}
