package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.service.UserActivationService;
import org.exchanger_bot.utils.CryptoTools;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Log4j
@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {


    private final CryptoTools cryptoTools;
    private final AppUserRepository appUserRepository;


    @Override
    public boolean activation(String cryptoUserId) {
        long id = cryptoTools.idOf(cryptoUserId);
        Optional<AppUser> appUserOptional = appUserRepository.findById(id);
        if (appUserOptional.isPresent()){
            AppUser appUser = appUserOptional.get();
            appUser.setActive(true);
            appUserRepository.save(appUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsUser(String cryptoId) {
        long id = 0;
        try { // если пользователь поменял хэш и не получилось декодировать в число
            id = cryptoTools.idOf(cryptoId);
        } catch (NumberFormatException e) {
            return false;
        }
        // если пользователь поменял хэш и все-таки удалось получить число(маловероятно)
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);

        if (!optionalAppUser.isPresent()){
            return false;
        }
        return true;
    }
}
