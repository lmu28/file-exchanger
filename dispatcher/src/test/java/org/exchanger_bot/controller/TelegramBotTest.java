package org.exchanger_bot.controller;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.exchanger_bot.service.UpdateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TelegramBotTest {


    public static final String BOT_NAME = "botName";
    public static final String TOKEN = "token";
    public static final String BOT_URI = "bot_botUri";
    @InjectMocks
    @Spy
    TelegramBot telegramBot;
    @Mock
    private UpdateProcessor updateProcessor;

    @Mock
    private SetWebhook setWebhook;

    @Mock
    private Update update;

    @Mock
    private SendMessage sendMessage;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() throws TelegramApiException {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(telegramBot, "botName", BOT_NAME);
        ReflectionTestUtils.setField(telegramBot, "botToken", TOKEN);
        ReflectionTestUtils.setField(telegramBot, "botUri", BOT_URI);

        doReturn(message).when(telegramBot).execute(any(SendMessage.class));
//        ReflectionTestUtils.setField(updateProcessor,"telegramBot",telegramBot);

//        telegramBot.setWebhook(setWebhook);

    }

    @Test
    void getBotToken() {
        String actual = telegramBot.getBotToken();
        assertThat(actual).isEqualTo(TOKEN);
    }

    @Test
    void getBotUsername() {
        String actual = telegramBot.getBotUsername();
        assertThat(actual).isEqualTo(BOT_NAME);
    }

    @Test
    void onWebhookUpdateReceived() {
        assertThat(telegramBot.onWebhookUpdateReceived(update)).isNull();
    }

    @Test
    void getBotPath() {
        String actual = telegramBot.getBotPath();
        assertThat(actual).isNotEmpty();
    }

    @Test
    void executeSendMessage_CorrectExecute_Execute() throws TelegramApiException {
        telegramBot.executeSendMessage(sendMessage);
        verify(telegramBot).execute(sendMessage);
    }

    @Test
    void executeSendMessage_NotCorrectExecute_ThrowException() throws TelegramApiException {
        doThrow(new TelegramApiException()).when(telegramBot).execute(sendMessage);

        assertThatThrownBy(() ->  telegramBot.executeSendMessage(sendMessage))
                .isInstanceOf(RuntimeException.class);
        verify(telegramBot).execute(sendMessage);
    }



    @Test
    public void init_CorrectInit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, TelegramApiException {
        doNothing().when(telegramBot).setWebhook(any(SetWebhook.class));
        Method method = TelegramBot.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(telegramBot);

        verify(updateProcessor).registerBot(telegramBot);
        verify(telegramBot).setWebhook(any(SetWebhook.class));
    }


    @Test
    public void init_TelegramApiException_LogError() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, TelegramApiException {
        Logger logger = Logger.getLogger(TelegramBot.class);
        Appender appender = mock(Appender.class);
        logger.addAppender(appender);
        ArgumentCaptor<LoggingEvent> captor = ArgumentCaptor.forClass(LoggingEvent.class);
        doThrow(new TelegramApiException()).when(telegramBot).setWebhook(any(SetWebhook.class));

        Method method = TelegramBot.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(telegramBot);

        verify(updateProcessor).registerBot(telegramBot);
        verify(appender).doAppend(captor.capture());
        assertThat(Level.ERROR).isEqualTo(captor.getAllValues().get(0).getLevel());
    }
}