package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.exchanger_bot.model.RabbitQueue.ANSWER_MESSAGE;


@RequiredArgsConstructor
@Service
@Log4j
public class ProducerServiceImpl implements ProducerService {

    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.queues.answer-message}")
    private String answerMessageQueue;


    @Override
    public void produceAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(answerMessageQueue,sendMessage);
    }
}
