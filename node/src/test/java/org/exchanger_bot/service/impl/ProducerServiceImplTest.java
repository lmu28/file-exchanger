package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ProducerServiceImplTest {

    public static final String QUEUE = "Queue";
    @InjectMocks
    ProducerServiceImpl producerService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(producerService,"answerMessageQueue",
                QUEUE);

    }

    @Test
    void produceAnswer() {
        producerService.produceAnswer(sendMessage);
        verify(rabbitTemplate).convertAndSend(QUEUE,sendMessage);
    }
}