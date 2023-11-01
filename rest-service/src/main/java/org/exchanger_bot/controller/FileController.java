package org.exchanger_bot.controller;


import lombok.RequiredArgsConstructor;
import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;





    @GetMapping(value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id){

        AppDocument appDocument = fileService.getDocument(id);
        //TODO добавить провреку что ошибка могла произоойти на строне сервера
        if(appDocument == null){
            return ResponseEntity.badRequest().build();
        }
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(
                appDocument.getBinaryContent());

        if (fileSystemResource == null){
            ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(appDocument.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + appDocument.getDocName())
                .body(fileSystemResource);
    }



    @GetMapping(value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id){

        AppPhoto appPhoto = fileService.getPhoto(id);
        //TODO добавить провреку что ошибка могла произоойти на строне сервера
        if(appPhoto == null){
            return ResponseEntity.badRequest().build();
        }
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(
                appPhoto.getBinaryContent());

        if (fileSystemResource == null){
            ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-disposition", "attachment; filename=" + System.currentTimeMillis() +".png")
                .body(fileSystemResource);
    }




}
