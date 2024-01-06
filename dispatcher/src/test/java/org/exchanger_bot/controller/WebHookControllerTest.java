package org.exchanger_bot.controller;

import org.exchanger_bot.service.UpdateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class WebHookControllerTest {

    @InjectMocks
    private WebHookController webHookController;
    @Mock
    private  UpdateProcessor updateProcessor;

    @Mock
    private Update update;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void onUpdateReceived_ReturnsValidResponseEntity() {
        ResponseEntity<?> responseEntity = webHookController.onUpdateReceived(update);

        verify(updateProcessor).processUpdate(update);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}