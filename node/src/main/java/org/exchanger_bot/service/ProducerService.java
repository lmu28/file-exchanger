package org.exchanger_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ProducerService {

    void produceAnswer(SendMessage sendMessage);
}
