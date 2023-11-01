package org.exchanger_bot.service;


import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);

    AppPhoto getPhoto(String id);

    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}

