package org.exchanger_bot.service;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage);

    AppPhoto processPhoto(Message externalMessage);

    String generateDownloadLink(long id, LinkType linkType);
}
