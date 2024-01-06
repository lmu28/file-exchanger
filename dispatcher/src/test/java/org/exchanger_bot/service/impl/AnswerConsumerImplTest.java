package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.AnswerConsumer;
import org.exchanger_bot.service.UpdateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AnswerConsumerImplTest {


    @InjectMocks
    private AnswerConsumerImpl answerConsumer;
    @Mock
    private UpdateProcessor updateProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void consume_CallsSetView() {
        SendMessage sendMessage = new SendMessage();
        answerConsumer.consume(sendMessage);

        verify(updateProcessor, times(1)).setView(sendMessage);
    }
}