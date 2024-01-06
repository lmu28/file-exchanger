package org.exchanger_bot.service.impl;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.service.ProducerMailService;
import org.exchanger_bot.service.ProducerService;
import org.exchanger_bot.utils.CryptoTools;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.exchanger_bot.model.enums.AppUserState.BASIC;
import static org.exchanger_bot.model.enums.AppUserState.WAIT_FOR_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserManipulationServiceImplTest {

    public static final String EMAIL = "testEmail@Mail.ru";
    public static final String CRYPTO_ID = "cryptoId";
    public static final long ID = 0;
    public static final String QUEUE_NAME = "queueName";
    @InjectMocks
    private UserManipulationServiceImpl userManipulationService;

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private CryptoTools cryptoTools;
    @Mock
    private ProducerService producerService;
    @Mock
    private ProducerMailService producerMailService;
    @Mock
    private AppUser appUser;

    @Mock
    private MailActivationResp resp;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userManipulationService,
                "verifyingEmailQueue", QUEUE_NAME);
        doReturn(BASIC).when(appUser).getState();
        doReturn(BASIC).when(appUser).getState();
        doReturn(appUser).when(appUserRepository).save(appUser);
        doReturn(ID).when(cryptoTools).idOf(CRYPTO_ID);
        doReturn(EMAIL).when(resp).getEmail();
        doReturn(Optional.of(appUser)).when(appUserRepository).findById(ID);


    }

    @Test
    void registerUser_ActiveUser() {
        doReturn(true).when(appUser).isActive();
        String ans = userManipulationService.registerUser(appUser);
        assertThat(ans).isNotEmpty();
    }

    @Test
    void registerUser_EmailIsNotNull() {
        doReturn(false).when(appUser).isActive();
        doReturn(EMAIL).when(appUser).getEmail();
        String ans = userManipulationService.registerUser(appUser);
        assertThat(ans).isNotEmpty();
    }


    @Test
    void registerUser_NotActiveUser() {
        doReturn(false).when(appUser).isActive();
        doReturn(null).when(appUser).getEmail();
        String ans = userManipulationService.registerUser(appUser);

        verify(appUserRepository).save(appUser);
        verify(appUser).setState(WAIT_FOR_EMAIL);
        assertThat(ans).isNotEmpty();
    }

    @Test
    void setEmail_NotCorrectEmail() {
        String incorrectEmail = "13123131";
        String ans = userManipulationService.setEmail(appUser, incorrectEmail);
        assertThat(ans).isNotEmpty();
    }


    @Test
    void setEmail_AppUserIsPresent() {
        doReturn(Optional.of(appUser)).when(appUserRepository).findByEmail(EMAIL);
        String ans = userManipulationService.setEmail(appUser, EMAIL);
        assertThat(ans).isNotEmpty();
    }


    @Test
    void setEmail_AppUserIsNotPresent() {
        doReturn(Optional.empty()).when(appUserRepository).findByEmail(EMAIL);
        String ans = userManipulationService.setEmail(appUser, EMAIL);
        doReturn(CRYPTO_ID).when(cryptoTools).hashOf(ID);

        verify(cryptoTools).hashOf(ID);
        verify(producerMailService).produce(anyString(), any(UserMailInfo.class));
        assertThat(ans).isNotEmpty();
    }

    @Test
    void processIsActivatedEmail_AppUserIsNotPresent_LogError() {
        doReturn(Optional.empty()).when(appUserRepository).findById(ID);
        Logger logger = Logger.getLogger(UserManipulationServiceImpl.class);
        Appender appender = mock(Appender.class);
        logger.addAppender(appender);
        ArgumentCaptor<LoggingEvent> captor = ArgumentCaptor.forClass(LoggingEvent.class);

        userManipulationService.processIsActivatedEmail(resp);

        verify(appender).doAppend(captor.capture());
        assertThat(Level.ERROR).isEqualTo(captor.getAllValues().get(0).getLevel());
        verify(producerService).produceAnswer(any(SendMessage.class));

    }


    @Test
    void processIsActivatedEmail_EmailIsSent() {
        doReturn(true).when(resp).isSentEmail();


        userManipulationService.processIsActivatedEmail(resp);

        verify(appUser).setEmail(anyString());
        verify(appUserRepository).save(appUser);
        verify(producerService).produceAnswer(any(SendMessage.class));

    }

    @Test
    void processIsActivatedEmail_EmailIsNonSent() {
        doReturn(false).when(resp).isSentEmail();


        userManipulationService.processIsActivatedEmail(resp);

        verify(appUserRepository).save(appUser);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }
}