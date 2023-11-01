package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.model.RowData;
import org.exchanger_bot.service.*;
import org.exchanger_bot.service.enums.LinkType;
import org.exchanger_bot.service.exceptions.UploadFileException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.exchanger_bot.model.enums.AppUserState.BASIC;
import static org.exchanger_bot.model.enums.AppUserState.WAIT_FOR_EMAIL;
import static org.exchanger_bot.service.enums.UserCommand.*;


@Service
@RequiredArgsConstructor
@Log4j
public class MainServiceImpl implements MainService {
    //TODO реализовать систему миграций баз данных. повешать index на поле email

    private final ProducerService producerService;
    private final RowDataService rowDataService;

    private final AppUserService appUserService;

    private final FileService fileService;

    private final UserManipulationService userManipulationService;

    @Override
    public void processTextMassage(Update update) {
        saveRowData(update);
        AppUser appUser = findOrSaveAppUser(update);

        Message message = update.getMessage();
        String command = message.getText();
        String output = "";
        if (CANCEL.equals(command)) {
            output = cancelProcess(appUser);
        } else if (BASIC.equals(appUser.getState())) {
            output = processUserCommand(appUser,command);
        }else if (WAIT_FOR_EMAIL.equals(appUser.getState())) {
            output = userManipulationService.setEmail(appUser,command);
        }

        sendAnswer(output,message.getChatId());



    }

    @Override
    public void processDocumentMassage(Update update) {
        saveRowData(update);
        AppUser appUser = findOrSaveAppUser(update);
        long chatId = update.getMessage().getChatId();
        if (!isAllowToSendContent(chatId, appUser)) return;

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateDownloadLink(doc.getId(), LinkType.GET_DOC);
            String answer = "Документ успешно загружен! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }
    @Override
    public void processPhotoMassage(Update update) {
        saveRowData(update);
        AppUser appUser = findOrSaveAppUser(update);
        long chatId = update.getMessage().getChatId();
        if (!isAllowToSendContent(chatId, appUser)) return;

        try {
            AppPhoto appPhoto = fileService.processPhoto(update.getMessage());
            String link = fileService.generateDownloadLink(appPhoto.getId(), LinkType.GET_PHOTO);
            String answer = "Фото успешно загружено! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }

    }

    private void saveRowData(Update update) {
        rowDataService.save(RowData.builder()
                .id(0)
                .event(update)
                .build());

    }

    private AppUser findOrSaveAppUser(Update update) {
        Message message = update.getMessage();
        User telegramUser = message.getFrom();
        AppUser persistantAppUser = appUserService.findAppUserByTelegramUserId(telegramUser.getId());

        if (persistantAppUser == null) {

            AppUser transientAppUser = appUserService.save(AppUser.builder()
                    .id(0)
                    .telegramUserId(telegramUser.getId())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .username(telegramUser.getUserName())
                    .isActive(false)
                    .state(BASIC).build());

            return transientAppUser;


        }

        return persistantAppUser;

    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC);
        appUserService.save(appUser);
        return "Команда отменена";
    }

    private String help() {
        return "Список доступных команд:\n"
                + "/cancel - отмена выполнения текущей команды;\n"
                + "/registration - регистрация пользователя.";
    }

    private String processUserCommand(AppUser appUser,String command) {
        if (START.equals(command)) {
            return "Здравствуйте! посмотреть информацию о боте можно по команде /help";

        } else if (HELP.equals(command)) {
            return help();

        } else if (REGISTRATION.equals(command)) {
            return userManipulationService.registerUser(appUser);

        }else {
            return "Неизвестаня команда";
        }
    }

    private void sendAnswer(String answer, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(answer);
        sendMessage.setChatId(chatId);
        producerService.produceAnswer(sendMessage);
    }

    private boolean isAllowToSendContent(long chatId, AppUser appUser) {
        if (WAIT_FOR_EMAIL.equals(appUser.getState())){
            sendAnswer("Используйте команду /cancel для отправки контента",chatId);
            return false;
        }
        if (!appUser.isActive()){
            sendAnswer("Вам нужно зарегистрироваться - /registration",chatId);
            return false;
        }

        return true;

    }
}
