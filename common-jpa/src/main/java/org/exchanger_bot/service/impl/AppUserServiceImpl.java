package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.service.AppUserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    @Override
    @Transactional
    public AppUser findAppUserByTelegramUserId(long telegramId) {
        return appUserRepository.findByTelegramUserId(telegramId);
    }

    @Override
    public AppUser save(AppUser appUser) {


        return appUserRepository.save(appUser);
    }


    @Override
    public Optional<AppUser> findById(long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
