package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.model.enums.AppUserState;
import org.exchanger_bot.repository.AppUserRepository;
import org.exchanger_bot.service.ConsumerMailService;
import org.exchanger_bot.service.ProducerMailService;
import org.exchanger_bot.service.ProducerService;
import org.exchanger_bot.service.UserManipulationService;
import org.exchanger_bot.utils.CryptoTools;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;


@Service
@Log4j
@RequiredArgsConstructor
public class UserManipulationServiceImpl implements UserManipulationService {


    @Value("${spring.rabbitmq.queues.verifying-email}")
    private String verifyingEmailQueue;

    private final AppUserRepository appUserRepository;
    private final CryptoTools cryptoTools;
    private final ProducerService producerService;
    private final ProducerMailService producerMailService;



    @Value("${service.mail.uri}")
    private String mailServiceUri;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.isActive()) {
            return "Вы уже зарегистрированы";
        } else if (appUser.getEmail() != null) {
            return "Вам уже был отпрвавлена ссылка на электронную почту." +
                    "Для повторной отправки введи /retry_mail";
        }
        appUser.setState(AppUserState.WAIT_FOR_EMAIL);
        appUserRepository.save(appUser);
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
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);

        if (!optionalAppUser.isPresent()) {
            String cryptoUserId = cryptoTools.hashOf(appUser.getId());
            producerMailService.produce(verifyingEmailQueue, UserMailInfo.builder()
                    .id(cryptoUserId)
                    .email(email)
                    .build());
            return "Обработка запроса...";
        } else {
            return "Данный email уже используется. Введите другой.";
        }

    }


    @Override
    public void processIsActivatedEmail(MailActivationResp resp) {
        long id = cryptoTools.idOf(resp.getCryptoId());
        String email = resp.getEmail();
        SendMessage sendMessage = new SendMessage();


        Optional<AppUser> oAppUser = appUserRepository.findById(id);

        if(!oAppUser.isPresent()){
            log.error("Node: Error get appUser by id");
            sendMessage.setText( "Что-то пошло не так");
            producerService.produceAnswer(sendMessage);
            return;
        }
        AppUser appUser =  oAppUser.get();

        appUser.setState(AppUserState.BASIC);

        if (resp.isSentEmail()){
            appUser.setEmail(email);
            String msg = "Вам на почту было отправлено письмо."
                    + "Перейдите по ссылке в письме для подтверждения регистрации.";
            sendMessage.setText(msg);
        }else{
            String msg = String.format("Отправка эл. письма на почту %s не удалась.", email);
            sendMessage.setText(msg);
        }
        appUserRepository.save(appUser);
        sendMessage.setChatId(appUser.getTelegramUserId());
        producerService.produceAnswer(sendMessage);
    }


}
