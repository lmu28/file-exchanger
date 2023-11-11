package org.exchanger_bot.service;

import org.exchanger_bot.utils.dto.MailActivationResp;

public interface MailProducer {

    void produce(String rabbitQueue, MailActivationResp response);
}
