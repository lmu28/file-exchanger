package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.MailConsumer;
import org.exchanger_bot.service.MailProducer;
import org.exchanger_bot.service.MailService;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class MailConsumerImplTest {

    @InjectMocks
    private MailConsumerImpl mailConsumer;
    @Mock
    private MailService mailService;
    @Mock
    UserMailInfo info;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void consume_CallProcessUserMailInfo() {
        mailConsumer.consume(info);
        verify(mailService).processUserMailInfo(info);
    }
}