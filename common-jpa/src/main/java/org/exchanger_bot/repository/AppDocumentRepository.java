package org.exchanger_bot.repository;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppDocumentRepository extends JpaRepository<AppDocument,Long> {

}
