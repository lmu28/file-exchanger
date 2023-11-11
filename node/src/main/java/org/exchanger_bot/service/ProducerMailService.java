package org.exchanger_bot.service;

import org.exchanger_bot.utils.dto.UserMailInfo;

public interface ProducerMailService {

    void produce(String rabbitQueue, UserMailInfo userMailInfo);
}
