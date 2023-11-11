package org.exchanger_bot.service;

import org.exchanger_bot.utils.dto.MailActivationResp;

public interface ConsumerMailService {

    void consume(MailActivationResp mailActivationResp);
}
