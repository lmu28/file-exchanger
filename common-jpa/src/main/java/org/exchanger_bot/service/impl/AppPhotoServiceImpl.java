package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.repository.AppPhotoRepository;
import org.exchanger_bot.service.AppPhotoService;
import org.springframework.stereotype.Service;


@Service
@Log4j
@RequiredArgsConstructor
public class AppPhotoServiceImpl implements AppPhotoService {

    private final AppPhotoRepository repository;
    @Override
    public AppPhoto save(AppPhoto appPhoto) {
        return repository.save(appPhoto);
    }

    @Override
    public AppPhoto findById(long id) {
        return repository.findById(id).orElse(null);
    }
}
