package org.exchanger_bot.controller;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileControllerTest {


    public static final long ID = 0;
    public static final String STRING_ID = "0";
    public static final String FILE_NAME = "filename";
    public static final long FILE_SIZE = 255;
    public static final String MIME_TYPE = "multipart/form-data";
    public static final byte[] BYTES = new byte[255];
    @InjectMocks
    private  FileController fileController;

    @Mock
    private  FileService fileService;

    @Mock
    private BinaryContent binaryContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    void getDoc_PayloadIsValid_ReturnsValidResponseEntity() {
        doReturn(BYTES).when(binaryContent).getFileAsArrayOfBytes();
        AppDocument appDocument = AppDocument.builder()
                .id(ID)
                .docName(FILE_NAME)
                .fileSize(FILE_SIZE)
                .mimeType(MIME_TYPE)
                .binaryContent(binaryContent)
                .build();
        doReturn(appDocument).when(fileService).getDocument(STRING_ID);

        ResponseEntity<?> responseEntity = fileController.getDoc(STRING_ID);

        verify(fileService).getDocument(STRING_ID);
        assertAll(
                () -> assertThat(responseEntity).isNotNull(),
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.MULTIPART_FORM_DATA),
                () -> assertThat(responseEntity.getBody()).isEqualTo(new byte[255])
        );
    }


    @Test
    void getDoc_PayloadIsNull_ReturnsValidResponseEntity() {

        doReturn(null).when(fileService).getDocument(STRING_ID);

        ResponseEntity<?> responseEntity = fileController.getDoc(STRING_ID);

        verify(fileService).getDocument(STRING_ID);
        assertAll(
                () -> assertThat(responseEntity).isNotNull(),
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }


    @Test
    void getPhoto_PayloadIsValid_ReturnsValidResponseEntity() {
        doReturn(BYTES).when(binaryContent).getFileAsArrayOfBytes();
        AppPhoto appPhoto = AppPhoto.builder()
                .id(ID)
                .fileSize((int)FILE_SIZE)
                .binaryContent(binaryContent)
                .build();
        doReturn(appPhoto).when(fileService).getPhoto(STRING_ID);

        ResponseEntity<?> responseEntity = fileController.getPhoto(STRING_ID);

        verify(fileService).getPhoto(STRING_ID);
        assertAll(
                () -> assertThat(responseEntity).isNotNull(),
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG),
                () -> assertThat(responseEntity.getBody()).isEqualTo(new byte[255])
        );

    }


    @Test
    void getPhoto_PayloadIsNull_ReturnsValidResponseEntity() {

        doReturn(null).when(fileService).getPhoto(STRING_ID);

        ResponseEntity<?> responseEntity = fileController.getPhoto(STRING_ID);

        verify(fileService).getPhoto(STRING_ID);
        assertAll(
                () -> assertThat(responseEntity).isNotNull(),
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }
}