package org.exchanger_bot.repository;

import org.exchanger_bot.model.RowData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RowDataRepository extends JpaRepository<RowData,Long> {
}
