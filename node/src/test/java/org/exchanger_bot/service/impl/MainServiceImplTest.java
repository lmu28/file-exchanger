package org.exchanger_bot.service.impl;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.exchanger_bot.model.*;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.repository.RowDataRepository;
import org.exchanger_bot.service.*;
import org.exchanger_bot.service.enums.LinkType;
import org.exchanger_bot.service.exceptions.UploadFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;


import static org.assertj.core.api.Assertions.assertThat;
import static org.exchanger_bot.model.enums.AppUserState.BASIC;
import static org.exchanger_bot.model.enums.AppUserState.WAIT_FOR_EMAIL;
import static org.exchanger_bot.service.enums.UserCommand.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MainServiceImplTest {


    public static final long ID = 0;
    public static final String ANSWER_MESSAGE = "Answer message";
    public static final String EMAIL = "someEmail@mail.ru";
    public static final String LINK = "link";
    @InjectMocks
    private MainServiceImpl mainService;


    @Mock
    private Update update;

    @Mock
    private AppUser appUser;
    @Mock
    private RowData rowData;

    @Mock
    private User telegramUser;
    @Mock
    private Message message;

    @Mock
    private AppPhoto appPhoto;

    @Mock
    private AppDocument appDoc;


    @Mock
    private ProducerService producerService;
    @Mock
    private RowDataRepository rowDataRepository;
    @Mock

    private AppUserRepository appUserRepository;
    @Mock
    private FileService fileService;
    @Mock
    private UserManipulationService userManipulationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(telegramUser);

        when(telegramUser.getId()).thenReturn(ID);
        when(appUserRepository.findByTelegramUserId(ID)).thenReturn(appUser);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(rowDataRepository.save(rowData)).thenReturn(rowData);
        when(userManipulationService.registerUser(any(AppUser.class))).thenReturn(ANSWER_MESSAGE);
        when(userManipulationService.setEmail(eq(appUser),anyString())).thenReturn(ANSWER_MESSAGE);
        when(fileService.processDoc(message)).thenReturn(appDoc);
        when(fileService.processPhoto(message)).thenReturn(appPhoto);
        when(fileService.generateDownloadLink(ID, LinkType.GET_DOC)).thenReturn(LINK);
        when(fileService.generateDownloadLink(ID, LinkType.GET_PHOTO)).thenReturn(LINK);




    }


    @Test
    void processTextMassage_CancelCommand() {
        when(message.getText()).thenReturn(CANCEL.toString());
        mainService.processTextMassage(update);
        verify(appUser).setState(BASIC);
        verify(appUserRepository).save(appUser);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));

    }

    @Test
    void processTextMassage_CancelCommandAppUserDoesNotPersists() {
        when(message.getText()).thenReturn(CANCEL.toString());
        when(appUserRepository.findByTelegramUserId(ID)).thenReturn(null);
        mainService.processTextMassage(update);
        verify(appUser).setState(BASIC);
        verify(appUserRepository).save(appUser);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processTextMassage_StartCommand() {
        when(appUser.getState()).thenReturn(BASIC);
        when(message.getText()).thenReturn(START.toString());
        mainService.processTextMassage(update);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processTextMassage_HelpCommand() {
        when(appUser.getState()).thenReturn(BASIC);
        when(message.getText()).thenReturn(HELP.toString());
        mainService.processTextMassage(update);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processTextMassage_RegistrationCommand() {
        when(appUser.getState()).thenReturn(BASIC);
        when(message.getText()).thenReturn(REGISTRATION.toString());
        mainService.processTextMassage(update);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(userManipulationService).registerUser(any(AppUser.class));
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processTextMassage_RetryEmailCommand() {
        when(appUser.getState()).thenReturn(BASIC);
        when(appUser.getEmail()).thenReturn(EMAIL);
        when(message.getText()).thenReturn(RETRY_EMAIL.toString());
        mainService.processTextMassage(update);
        verify(appUser).setState(WAIT_FOR_EMAIL);
        verify(appUser).setEmail(null);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(appUserRepository).save(appUser);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processTextMassage_UnknownCommand() {
        when(appUser.getState()).thenReturn(BASIC);
        when(message.getText()).thenReturn("");
        mainService.processTextMassage(update);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processTextMassage_WaitForEmailState() {
        when(appUser.getState()).thenReturn(WAIT_FOR_EMAIL);
        when(message.getText()).thenReturn("");
        mainService.processTextMassage(update);
        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
        verify(userManipulationService).setEmail(eq(appUser),anyString());
    }

    @Test
    void processDocMessage_WaitForEmailState() {
        when(appUser.getState()).thenReturn(WAIT_FOR_EMAIL);
        when(message.getText()).thenReturn("");


        mainService.processDocumentMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processDocMessage_NotActiveUser() {
        when(appUser.isActive()).thenReturn(false);
        when(message.getText()).thenReturn("");


        mainService.processDocumentMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processDocMessage_AllowToSendMessage() {
        when(appUser.isActive()).thenReturn(true);
        when(message.getText()).thenReturn("");


        mainService.processDocumentMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(fileService).processDoc(message);
        verify(fileService).generateDownloadLink(ID,LinkType.GET_DOC);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processDocMessage_UploadFileException_LogError() {
        Logger logger = Logger.getLogger(MainServiceImpl.class);
        Appender appender = Mockito.mock(Appender.class);
        logger.addAppender(appender);
        ArgumentCaptor<LoggingEvent> eventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        when(appUser.isActive()).thenReturn(true);
        when(message.getText()).thenReturn("");
        when(fileService.processDoc(message)).thenThrow(new UploadFileException());



        mainService.processDocumentMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(fileService).processDoc(message);
        verify(appender).doAppend(eventArgumentCaptor.capture());
        assertThat(Level.ERROR).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getLevel());
        verify(producerService).produceAnswer(any(SendMessage.class));
    }
//

    @Test
    void processPhotoMessage_WaitForEmailState() {
        when(appUser.getState()).thenReturn(WAIT_FOR_EMAIL);
        when(message.getText()).thenReturn("");


        mainService.processPhotoMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processPhotoMessage_NotActiveUser() {
        when(appUser.isActive()).thenReturn(false);
        when(message.getText()).thenReturn("");


        mainService.processPhotoMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }


    @Test
    void processPhotoMessage_AllowToSendMessage() {
        when(appUser.isActive()).thenReturn(true);
        when(message.getText()).thenReturn("");


        mainService.processPhotoMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(fileService).processPhoto(message);
        verify(fileService).generateDownloadLink(ID,LinkType.GET_PHOTO);
        verify(producerService).produceAnswer(any(SendMessage.class));
    }

    @Test
    void processPhotoMessage_UploadFileException_LogError() {
        Logger logger = Logger.getLogger(MainServiceImpl.class);
        Appender appender = Mockito.mock(Appender.class);
        logger.addAppender(appender);
        ArgumentCaptor<LoggingEvent> eventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        when(appUser.isActive()).thenReturn(true);
        when(message.getText()).thenReturn("");
        when(fileService.processPhoto(message)).thenThrow(new UploadFileException());



        mainService.processPhotoMassage(update);


        verify(appUserRepository).findByTelegramUserId(ID);
        verify(fileService).processPhoto(message);
        verify(appender).doAppend(eventArgumentCaptor.capture());
        assertThat(Level.ERROR).isEqualTo(eventArgumentCaptor.getAllValues().get(0).getLevel());
        verify(producerService).produceAnswer(any(SendMessage.class));
    }
}