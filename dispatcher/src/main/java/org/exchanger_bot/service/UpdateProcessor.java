package org.exchanger_bot.service;

import org.exchanger_bot.controller.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProcessor {
    void registerBot(TelegramBot telegramBot);
    void processUpdate(Update update);
    void setView(SendMessage sendMessage);
}
