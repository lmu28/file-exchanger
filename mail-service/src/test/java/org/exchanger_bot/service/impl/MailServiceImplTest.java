package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.MailProducer;
import org.exchanger_bot.service.MailService;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceImplTest {


    public static final String ID = "1";
    public static final String EMAIL = "test@mail.ru";
    public static final String URL = "http://test/user/activation?id={id}";
    public static final String EMAIL_FROM = "emailFrom@mail.ru";
    public static final String QUEUE = "queue";


    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MailProducer mailProducer;

    @Mock
    private UserMailInfo userMailInfo;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userMailInfo.getId()).thenReturn(ID);
        when(userMailInfo.getEmail()).thenReturn(EMAIL);
        when(userMailInfo.getId()).thenReturn(ID);


    }


    @Test
    void processUserMailInfo_CallSendAndProduce() {

        ReflectionTestUtils.setField(mailService, "activationUri", URL);
        ReflectionTestUtils.setField(mailService, "emailFrom", EMAIL_FROM);
        ReflectionTestUtils.setField(mailService, "emailAnswerQueue", QUEUE);
        mailService.processUserMailInfo(userMailInfo);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
        verify(mailProducer).produce(any(String.class), any(MailActivationResp.class));

    }


}