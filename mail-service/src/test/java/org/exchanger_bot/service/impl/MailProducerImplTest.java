package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.MailProducer;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class MailProducerImplTest {

    public static final String QUEUE = "queue";
    @InjectMocks
    private MailProducerImpl mailProducer;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MailActivationResp resp;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void produce_CallConvertAndSend() {
        mailProducer.produce(QUEUE,resp);
        verify(rabbitTemplate).convertAndSend(QUEUE,resp);
    }
}