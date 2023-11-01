package org.exchanger_bot.repository;

import org.exchanger_bot.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    AppUser findByTelegramUserId(long telegramId);
    Optional<AppUser> findById(long id);
    Optional<AppUser> findByEmail(String email);

}
