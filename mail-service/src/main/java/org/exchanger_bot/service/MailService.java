package org.exchanger_bot.service;


import org.exchanger_bot.utils.dto.MailActivationResp;
import org.exchanger_bot.utils.dto.UserMailInfo;

public interface MailService {

    MailActivationResp sendMessage(UserMailInfo userMailInfo);

    void processUserMailInfo(UserMailInfo userMailInfo);
}
