package org.exchanger_bot.service;

import org.exchanger_bot.model.AppUser;

import java.util.Optional;

public interface AppUserService {

    AppUser findAppUserByTelegramUserId(long telegramId);

    AppUser save(AppUser appUser);


    Optional<AppUser> findById(long id);
    Optional<AppUser> findByEmail(String email);
}
