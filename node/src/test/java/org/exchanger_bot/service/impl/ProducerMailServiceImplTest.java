package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ProducerMailServiceImplTest {

    public static final String QUEUE = "queue";
    @InjectMocks
    ProducerMailServiceImpl producerMailService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    UserMailInfo info;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void produce() {
        producerMailService.produce(QUEUE,info);
        verify(rabbitTemplate).convertAndSend(QUEUE,info);
    }
}