package org.exchanger_bot.service;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;

public interface AppPhotoService {
    AppPhoto save(AppPhoto appPhoto);

    AppPhoto findById(long id);
}
