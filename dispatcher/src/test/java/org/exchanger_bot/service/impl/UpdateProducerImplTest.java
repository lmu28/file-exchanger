package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.UpdateProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UpdateProducerImplTest {

    public static final String QUEUE = "queue";
    @InjectMocks
    private UpdateProducerImpl updateProducer;

    @Mock
    private RabbitTemplate rabbitTemplate;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void produce_CallsConvertAndSend() {
        Update update = new Update();
        updateProducer.produce(QUEUE,update);
        verify(rabbitTemplate, times(1)).convertAndSend(QUEUE,update);
    }
}