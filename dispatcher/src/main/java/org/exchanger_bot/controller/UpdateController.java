package org.exchanger_bot.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.UpdateProducer;
import org.exchanger_bot.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.exchanger_bot.model.RabbitQueue.*;

@Component
@Log4j
@RequiredArgsConstructor
public class UpdateController {

    public static final String UNSUPPORTED_MESSAGE_TYPE = "You have sent unsupported message type.";
    private static final String FILE_IN_PROCESS = "Your file in process...";
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if (update == null){
            log.error("Received update is null");
            return;
        }
        if (update.getMessage() != null){
            distributeMessagesByType(update);

        }else {
            log.error("Received message is null");
        }



    }

    private void distributeMessagesByType(Update update) {
        Message message = update.getMessage();
        if(message.hasText()){
            processTextMessage(update);
        } else if(message.hasDocument()){
            processDocumentMessage(update);
        } else if (message.hasPhoto()){
            processPhotoMessage(update);
        }else{
            setUnsupportedTypeMessageView(update);
        }
    }



    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE,update);
        setFileInProcessView(update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE,update);
        setFileInProcessView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
    }



    private void setUnsupportedTypeMessageView(Update update) {
        telegramBot.executeSendMessage(messageUtils.createSendMessage(update, UNSUPPORTED_MESSAGE_TYPE));
    }

    public void setView(SendMessage sendMessage){ // чтобы была возможность отправлять сообщения из других классов
        telegramBot.executeSendMessage(sendMessage);

    }

    private void setFileInProcessView(Update update){
        telegramBot.executeSendMessage(messageUtils.createSendMessage(update, FILE_IN_PROCESS));

    }
}
