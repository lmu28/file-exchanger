package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.repository.AppDocumentRepository;
import org.exchanger_bot.repository.AppPhotoRepository;
import org.exchanger_bot.service.FileService;
import org.exchanger_bot.utils.CryptoTools;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
@Log4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTools cryptoTools;


    @Override
    public AppDocument getDocument(String cryptoId) {
        long docId;
        try {
            docId = cryptoTools.idOf(cryptoId);
        } catch (NumberFormatException e) {
            return null;
        }
        return appDocumentRepository.findById(docId).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String cryptoId) {
        Long photoId;
        try {
           photoId = cryptoTools.idOf(cryptoId);
        } catch (NumberFormatException e) {
            return null;
        }
        return appPhotoRepository.findById(photoId).orElse(null);
    }

    @Override
    //TODO переписать способ считывания файла, тк при таком подходе может скопится много временных файлов
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".bin");
            tempFile.deleteOnExit();// добавить в очередь на удаление
            FileUtils.writeByteArrayToFile(tempFile, binaryContent.getFileAsArrayOfBytes());

            return new FileSystemResource(tempFile);


        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
