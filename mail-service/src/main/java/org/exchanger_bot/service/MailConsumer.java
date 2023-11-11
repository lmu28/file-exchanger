package org.exchanger_bot.service;

import org.exchanger_bot.utils.dto.UserMailInfo;

public interface MailConsumer {


    void consume(UserMailInfo userMailInfo);
}
