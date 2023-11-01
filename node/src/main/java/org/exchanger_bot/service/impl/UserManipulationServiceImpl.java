package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.model.enums.AppUserState;
import org.exchanger_bot.service.AppUserService;
import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.CryptoTools;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;


@Service
@Log4j
@RequiredArgsConstructor
public class UserManipulationServiceImpl implements UserManipulationService {

    private final AppUserService appUserService;
    private final CryptoTools cryptoTools;


    @Value("${service.mail.uri}")
    private String mailServiceUri;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.isActive()) {
            return "Вы уже зарегистрированы";
        } else if (appUser.getEmail() != null) {
            //TODO предусмотерть возможность отправки повторного email
            return "Вам уже было отправлено письмо на электронную почту";
        }


        appUser.setState(AppUserState.WAIT_FOR_EMAIL);
        appUserService.save(appUser);
        return "Введите email";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return "Введите, пожалуйста, корректный email. Для отмены команды введите /cancel";
        }
        Optional<AppUser> optionalAppUser = appUserService.findByEmail(email);

        if (!optionalAppUser.isPresent()) {


            appUser.setEmail(email);
            appUser.setState(AppUserState.BASIC);
            appUser = appUserService.save(appUser);

            String cryptoUserId = cryptoTools.hashOf(appUser.getId());
            ResponseEntity<?> response = sendRequestToMailService(cryptoUserId, email);


            if (response.getStatusCode() != HttpStatus.OK) {
                String msg = String.format("Отправка эл. письма на почту %s не удалась.", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserService.save(appUser);
                return msg;
            }


            return "Вам на почту было отправлено письмо."
                    + "Перейдите по ссылке в письме для подтверждения регистрации.";

        } else {
            return "Данный email уже используется. Введите другой.";
        }

    }

    private ResponseEntity<?> sendRequestToMailService(String cryptoUserId, String email) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserMailInfo userMailInfo = UserMailInfo.builder()
                .id(cryptoUserId)
                .email(email)
                .build();

        HttpEntity<?> request = new HttpEntity<>(userMailInfo, headers);

        return restTemplate.exchange(mailServiceUri
                , HttpMethod.POST
                , request
                , String.class);

    }
}
