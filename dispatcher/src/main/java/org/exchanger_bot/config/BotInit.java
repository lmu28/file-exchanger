package org.exchanger_bot.config;

import org.exchanger_bot.controller.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;


@Component
public class BotInit {

    private final TelegramBot bot;

    @Value("${bot.uri}")
    private String botUri;

    public BotInit(TelegramBot bot) {
        this.bot = bot;
    }

//    @EventListener({ContextRefreshedEvent.class})
//    public void registryBot() {
//        TelegramBotsApi telegramBotsApi = null;
//
//        try {
//            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(bot);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }



    @EventListener({ContextRefreshedEvent.class})
    public void registryBot() {
        try {
            Webhook webhook = new DefaultWebhook();
            webhook.setInternalUrl(botUri);
            webhook.registerWebhook((WebhookBot) bot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


