package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.AnswerConsumer;
import org.exchanger_bot.service.UpdateProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Service
@RequiredArgsConstructor
@Log4j
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateProcessor updateProcessor;

    @RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
    @Override
    public void consume(SendMessage sendMessage) {
//        log.info("Dispatcher: Answer message is received");
        updateProcessor.setView(sendMessage);

    }








}
