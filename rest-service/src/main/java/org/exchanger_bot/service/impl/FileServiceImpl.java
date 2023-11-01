package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.service.AppDocumentService;
import org.exchanger_bot.service.AppPhotoService;
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


    private final AppDocumentService appDocumentService;
    private final AppPhotoService appPhotoService;
    private final CryptoTools cryptoTools;


    @Override
    public AppDocument getDocument(String id) {
        Long docId = cryptoTools.idOf(id);
        if (docId == null) return null;
        return appDocumentService.findById(docId);
    }

    @Override
    public AppPhoto getPhoto(String id) {
        Long photoId = cryptoTools.idOf(id);
        if (photoId == null) return null;
        return appPhotoService.findById(photoId);
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
