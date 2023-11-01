package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.exchanger_bot.model.RowData;
import org.exchanger_bot.repository.RowDataRepository;
import org.exchanger_bot.service.RowDataService;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class RowDataServiceImpl implements RowDataService {

    private final RowDataRepository rowDataRepository;

    @Override
    public RowData save(RowData r) {
        return rowDataRepository.save(r);
    }
}
