package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.service.exceptions.UploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


@Service
@Log4j
@RequiredArgsConstructor
public class FileDownloaderImpl implements org.exchanger_bot.service.FileDownloader {


    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    @Value("${bot.token}")
    private String token;


    @Override
    public byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}",token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = createUrl(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }
    }


    protected URL createUrl(String fullUri) throws MalformedURLException {
        return new URL(fullUri);
    }
}
