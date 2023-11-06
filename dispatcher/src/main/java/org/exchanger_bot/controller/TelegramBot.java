package org.exchanger_bot.controller;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Log4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramWebhookBot {

    // TODO think about когда dispatcher выключен tg bot все равно скапливает сообщения и когда мы включаем
    // Dispatcher все эти сообщения через webHook поступают в приложение


    @Value("${bot.name}")
    private  String botName;


    @Value("${bot.token}")
    private  String botToken;


    @Value("${bot.uri}")
    private String botUri;


    private final UpdateProcessor updateProcessor;



    @PostConstruct
    private void init(){
        updateProcessor.registerBot(this);


        try {
            SetWebhook setWebhook = new SetWebhook(botUri);
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error("Error setWebhook to bot");
            throw new RuntimeException(e);
        }
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
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    public void executeSendMessage(SendMessage sendMessage){
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
    }


}
