package org.exchanger_bot.controller;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Log4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    @Value("${bot.name}")
    private  String botName;


    @Value("${bot.token}")
    private  String botToken;


    private final UpdateController updateController;



    @PostConstruct
    private void init(){
        updateController.registerBot(this);
    }


    @Override
    public String getBotToken() {
        return botToken;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public void onUpdateReceived(Update update) {

        updateController.processUpdate(update);

    }



    public void executeSendMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
