package org.exchanger_bot.service;

import org.exchanger_bot.model.AppUser;

public interface UserManipulationService {

    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);
}
