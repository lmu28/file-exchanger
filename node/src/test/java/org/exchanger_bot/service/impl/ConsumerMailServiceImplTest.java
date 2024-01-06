package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.ConsumerMailService;
import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ConsumerMailServiceImplTest {

    @InjectMocks
    ConsumerMailServiceImpl consumerMailService;

    @Mock
    UserManipulationService userManipulationService;

    @Mock
    MailActivationResp resp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void consume_CallProcessIsActivatedEmail() {
        consumerMailService.consume(resp);
        verify(userManipulationService).processIsActivatedEmail(resp);
    }
}