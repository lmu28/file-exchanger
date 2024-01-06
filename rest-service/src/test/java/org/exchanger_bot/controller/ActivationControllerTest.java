package org.exchanger_bot.controller;

import org.exchanger_bot.service.UserActivationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class ActivationControllerTest {

    public static final String STRING_ID = "0";

    @InjectMocks
    private  ActivationController activationController;

    @Mock
    private  UserActivationService userActivationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void activate_UserNotExists_ReturnsValidResponseEntity() {
        doReturn(false).when(userActivationService).existsUser(STRING_ID);

        ResponseEntity<?> re = activationController.activate(STRING_ID);

        assertAll(

                () -> assertThat(re).isNotNull(),
                () -> assertThat(re.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
        );

    }

    @Test
    void activate_UserActivated_ReturnsValidResponseEntity() {
        doReturn(true).when(userActivationService).existsUser(STRING_ID);
        doReturn(true).when(userActivationService).activation(STRING_ID);

        ResponseEntity<?> re = activationController.activate(STRING_ID);

        assertAll(

                () -> assertThat(re).isNotNull(),
                () -> assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK)
        );
    }

    @Test
    void activate_OccurredServerError_ReturnsValidResponseEntity() {
        doReturn(true).when(userActivationService).existsUser(STRING_ID);
        doReturn(false).when(userActivationService).activation(STRING_ID);

        ResponseEntity<?> re = activationController.activate(STRING_ID);

        assertAll(

                () -> assertThat(re).isNotNull(),
                () -> assertThat(re.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        );


    }
}