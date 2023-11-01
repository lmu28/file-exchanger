package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.service.AppUserService;
import org.exchanger_bot.service.UserActivationService;
import org.exchanger_bot.utils.CryptoTools;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Log4j
@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {


    private final CryptoTools cryptoTools;
    private final AppUserService appUserService;


    @Override
    public boolean activation(String cryptoUserId) {
        long id = cryptoTools.idOf(cryptoUserId);
        Optional<AppUser> appUserOptional = appUserService.findById(id);
        if (appUserOptional.isPresent()){
            AppUser appUser = appUserOptional.get();
            appUser.setActive(true);
            appUserService.save(appUser);
            return true;
        }
        return false;
    }
}
