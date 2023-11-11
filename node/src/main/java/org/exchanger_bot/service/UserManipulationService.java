package org.exchanger_bot.service;

import org.exchanger_bot.model.AppUser;
import org.exchanger_bot.utils.dto.MailActivationResp;

public interface UserManipulationService {

    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);

    void processIsActivatedEmail(MailActivationResp resp);
}
