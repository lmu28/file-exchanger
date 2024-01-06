package org.exchanger_bot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.repository.AppDocumentRepository;
import org.exchanger_bot.repository.AppPhotoRepository;
import org.exchanger_bot.repository.BinaryContentRepository;
import org.exchanger_bot.service.*;
import org.exchanger_bot.service.enums.LinkType;
import org.exchanger_bot.service.exceptions.UploadFileException;
import org.exchanger_bot.utils.CryptoTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


@Service
@Log4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    @Value("${service.file_info.uri}")
    private String fileInfoUri;





    @Value("${bot.token}")
    private String token;


    @Value("${link.address}")
    private String linkAddress;

    private final BinaryContentRepository binaryContentRepository;
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final RestTemplate restTemplate;
    private final CryptoTools cryptoTools;
    private final FileDownloader fileDownloader;

    @Override
    public AppDocument processDoc(Message externalMessage) {
        String fileId = externalMessage.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);

        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getFilePathFromJson(response);
            byte[] fileInByte = fileDownloader.downloadFile(filePath);
            BinaryContent persistentBinaryContent =  buildAndSaveBinaryContent(fileInByte);

            Document telegramDoc = externalMessage.getDocument();
            AppDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return appDocumentRepository.save(transientAppDoc);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }


    @Override
    public AppPhoto processPhoto(Message externalMessage) {
        int photoListSize = externalMessage.getPhoto().size();
        int index =  photoListSize > 0 ? photoListSize - 1 : 0;
        PhotoSize telegramPhoto = externalMessage.getPhoto().get(index);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);

        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getFilePathFromJson(response);
            byte[] fileInByte = fileDownloader.downloadFile(filePath);
            BinaryContent persistentBinaryContent =  buildAndSaveBinaryContent(fileInByte);

            AppPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoRepository.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public String generateDownloadLink(long id, LinkType linkType) {
        String hash = cryptoTools.hashOf(id);
        return  "http://" + linkAddress + "/files/" + linkType + "?id=" + hash;

    }

    private ResponseEntity<String> getFilePath(String fileId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }



    private String getFilePathFromJson(ResponseEntity<String> response){
        JSONObject jsonObject = new JSONObject(response.getBody());
        String filePath = jsonObject
                .getJSONObject("result")
                .getString("file_path");

        return filePath;
    }

    private AppDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return AppDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    private AppPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return AppPhoto.builder()
                .id(0)
                .binaryContent(persistentBinaryContent)
                .telegramFileId(telegramPhoto.getFileId())
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    private BinaryContent buildAndSaveBinaryContent(byte[] fileInByte) {
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        return binaryContentRepository.save(transientBinaryContent);
    }


}

