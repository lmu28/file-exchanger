package org.exchanger_bot.service.impl;


import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.exchanger_bot.config.RabbitConfig;
import org.exchanger_bot.controller.TelegramBot;
import org.exchanger_bot.service.UpdateProcessor;
import org.exchanger_bot.service.UpdateProducer;
import org.exchanger_bot.utils.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


class UpdateProcessorImplTest {
    private final static String TEXT_MESSAGE_UPDATE_QUEUE = "text_message_update";

    private final static String DOC_MESSAGE_UPDATE_QUEUE = "doc_message_update";


    private final static String PHOTO_MESSAGE_UPDATE_QUEUE = "photo_message_update";


    private static final String FILE_IN_PROCESS = "Your file in process...";
    private static final String UNSUPPORTED_MESSAGE_TYPE = "You have sent unsupported message type.";

    public static final String RECEIVED_UPDATE_IS_NULL = "Received update is null";
    public static final String RECEIVED_MESSAGE_IS_NULL = "Received message is null";


    @InjectMocks
    private UpdateProcessorImpl updateProcessor;

    @Mock
    private UpdateProducer updateProducer;
    @Mock
    private RabbitConfig rabbitConfig;

    @Mock
    private MessageUtils messageUtils;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private SendMessage sendMessage;




    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        updateProcessor.registerBot(telegramBot);

        when(rabbitConfig.getTextMessageUpdateQueue()).thenReturn(TEXT_MESSAGE_UPDATE_QUEUE);
        when(rabbitConfig.getDocMessageUpdateQueue()).thenReturn(DOC_MESSAGE_UPDATE_QUEUE);
        when(rabbitConfig.getPhotoMessageUpdateQueue()).thenReturn(PHOTO_MESSAGE_UPDATE_QUEUE);
        when(messageUtils.createSendMessage(any(Update.class),anyString())).thenReturn(sendMessage);

    }



    @Test
    void registerBot_SetTelegramBot() {
        TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
        updateProcessor.registerBot(telegramBot);
        TelegramBot internalBot;
        try {
            internalBot = (TelegramBot) FieldUtils.readField(updateProcessor,
                    "telegramBot", true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertThat(internalBot).isEqualTo(telegramBot);
    }

    @Test
    void processUpdate_UpdateIsNull_LogError() {
        Logger logger = Logger.getLogger(UpdateProcessorImpl.class);
        Appender appender = Mockito.mock(Appender.class);
        logger.addAppender(appender);

        updateProcessor.processUpdate(null);

        //только захват логирования
        ArgumentCaptor<LoggingEvent> eventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        // appender добавил (.doAppend) то , что сохранил в себе eventArgumentCaptor(.capture)
        verify(appender, times(1)).doAppend(eventArgumentCaptor.capture());
        assertThat(RECEIVED_UPDATE_IS_NULL).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getMessage());
        assertThat(Level.ERROR).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getLevel());


    }

    @Test
    void processUpdate_MessageIsNull_LogError() {
        Update update = new Update();
        Message message = null;
        update.setMessage(message);
        Logger logger = Logger.getLogger(UpdateProcessorImpl.class);
        Appender appender = Mockito.mock(Appender.class);
        logger.addAppender(appender);

        updateProcessor.processUpdate(update);

        ArgumentCaptor<LoggingEvent> eventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        verify(appender, times(1)).doAppend(eventArgumentCaptor.capture());
        assertThat(RECEIVED_MESSAGE_IS_NULL).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getMessage());
        assertThat(Level.ERROR).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getLevel());


    }


    @Test
    void processUpdate_TextMessage_CallsUpdateProducerWithTextQueue() {
        Update update = new Update();
        Message message = new Message();
        message.setText("test text");
        update.setMessage(message);
        updateProcessor.processUpdate(update);
        verify(updateProducer, times(1)).produce(TEXT_MESSAGE_UPDATE_QUEUE, update);
    }

    @Test
    void processUpdate_PhotoMessage_CallsUpdateProducerAndSetView() {
        Update update = new Update();
        Message message = new Message();
        message.setPhoto(List.of(new PhotoSize()));
        update.setMessage(message);
        updateProcessor.processUpdate(update);
        verify(updateProducer).produce(PHOTO_MESSAGE_UPDATE_QUEUE, update);
        verify(telegramBot).executeSendMessage(any(SendMessage.class));
        verify(messageUtils).createSendMessage(update, FILE_IN_PROCESS);
    }


    @Test
    void processUpdate_DocMessage_CallsUpdateProducerAndSetView() {
        Update update = new Update();
        Message message = new Message();
        message.setDocument(new Document());
        update.setMessage(message);
        updateProcessor.processUpdate(update);
        verify(updateProducer).produce(DOC_MESSAGE_UPDATE_QUEUE, update);
        verify(telegramBot).executeSendMessage(any(SendMessage.class));
        verify(messageUtils).createSendMessage(update, FILE_IN_PROCESS);
    }


    @Test
    void processUpdate_UnsupportedMessage_SetView() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        updateProcessor.processUpdate(update);
        verify(telegramBot).executeSendMessage(any(SendMessage.class));
        verify(messageUtils).createSendMessage(update, UNSUPPORTED_MESSAGE_TYPE);
    }



    @Test
    void setView() {
        SendMessage sendMessage = new SendMessage();
        updateProcessor.setView(sendMessage);
        verify(telegramBot).executeSendMessage(sendMessage);
    }
}