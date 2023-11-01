package org.exchanger_bot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.repository.AppDocumentRepository;
import org.exchanger_bot.repository.BinaryContentRepository;
import org.exchanger_bot.service.AppDocumentService;
import org.exchanger_bot.service.BinaryContentService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository repository;

    @Override
    @Transactional
    public BinaryContent save(BinaryContent binaryContent) {
        return repository.save(binaryContent);
    }
}
