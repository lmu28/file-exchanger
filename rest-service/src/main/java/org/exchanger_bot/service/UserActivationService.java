package org.exchanger_bot.service;


public interface UserActivationService {
    boolean activation(String cryptoUserId);

    boolean existsUser(String id);
}

