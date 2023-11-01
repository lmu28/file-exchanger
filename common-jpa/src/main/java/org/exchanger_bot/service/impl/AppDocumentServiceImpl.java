package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.repository.AppDocumentRepository;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.service.AppDocumentService;
import org.exchanger_bot.service.AppUserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
public class AppDocumentServiceImpl implements AppDocumentService {

    private final AppDocumentRepository repository;

    @Override
    @Transactional
    public AppDocument save(AppDocument appDocument) {
        return repository.save(appDocument);
    }

    @Override
    public AppDocument findById(long id) {
        return repository.findById(id).orElse(null);
    }
}
