package org.exchanger_bot.service;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppUser;

public interface AppDocumentService {
    AppDocument save(AppDocument appDocument);

    AppDocument findById(long id);
}
