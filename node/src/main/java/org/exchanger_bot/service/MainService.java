package org.exchanger_bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {

    void processTextMassage(Update update);
    void processDocumentMassage(Update update);
    void processPhotoMassage(Update update);
}
