package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.MainService;
import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ConsumerServiceImplTest {

    @InjectMocks
    ConsumerServiceImpl consumerService;

    @Mock
    MainService mainService;

    @Mock
    Update update;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void consumeTextMessageUpdate_CallConsumeTextMessageUpdate() {
        consumerService.consumeTextMessageUpdate(update);
        verify(mainService).processTextMassage(update);
    }

    @Test
    void consumeDocMessageUpdate_CallConsumeDocMessageUpdate() {
        consumerService.consumeDocMessageUpdate(update);
        verify(mainService).processDocumentMassage(update);
    }

    @Test
    void consumePhotoMessageUpdate_CallConsumePhotoMessageUpdate() {
        consumerService.consumePhotoMessageUpdate(update);
        verify(mainService).processPhotoMassage(update);
    }
}