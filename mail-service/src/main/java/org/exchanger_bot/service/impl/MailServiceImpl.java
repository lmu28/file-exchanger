package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.MailProducer;
import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;
import org.exchanger_bot.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {


    private final JavaMailSender javaMailSender;

    private final MailProducer mailProducer;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Value("${service.activation.uri}")
    private String activationUri;

    @Value("${spring.rabbitmq.queues.verifying-email-answer}")
    private String emailAnswerQueue;



    @Override
    public void processUserMailInfo(UserMailInfo userMailInfo){
        MailActivationResp resp = sendMessage(userMailInfo);

        mailProducer.produce(emailAnswerQueue,resp);

    }


    @Override
    public MailActivationResp sendMessage(UserMailInfo userMailInfo) {
        String subject = "Ссылка для активации";
        String emailTo = userMailInfo.getEmail();
        String userId = userMailInfo.getId();
        String message = buildMessage(userId);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setText(message);

        try {
            javaMailSender.send(simpleMailMessage);
            return buildMailActivationResp(userId,emailTo,true);
        } catch (MailException e) {
            log.error("MailService " + e.getMessage());
            return buildMailActivationResp(userId,emailTo,false);
        }

    }

    private String buildMessage(String id) {
        String msg = "Для прохождения регистарции перейдите по ссылке: " + activationUri;
        return  msg.replace("{id}",id);

    }


    private MailActivationResp buildMailActivationResp(String id, String email, boolean isSentMail){

        return MailActivationResp.builder()
                .CryptoId(id)
                .isSentEmail(isSentMail)
                .email(email)
                .build();

    }
}
