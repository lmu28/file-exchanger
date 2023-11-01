package org.exchanger_bot.service;


import org.exchanger_bot.utils.dto.UserMailInfo;

public interface MailService {

    void sendMessage(UserMailInfo userMailInfo);
}
