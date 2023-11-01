package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.exchanger_bot.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {


    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Value("${service.activation.uri}")
    private String activationUri;



    @Override
    public void sendMessage(UserMailInfo userMailInfo) {
        String subject = "Ссылка для активации";
        String emailTo = userMailInfo.getEmail();
        String message = buildMessage(userMailInfo.getId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setText(message);


        javaMailSender.send(simpleMailMessage);

    }

    private String buildMessage(String id) {
        String msg = "Для прохождения регистарции перейдите по ссылке: " + activationUri;
        return  msg.replace("{id}",id);

    }
}
