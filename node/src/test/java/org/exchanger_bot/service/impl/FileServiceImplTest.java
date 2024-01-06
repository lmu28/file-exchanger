package org.exchanger_bot.service.impl;

import org.exchanger_bot.model.AppDocument;
import org.exchanger_bot.model.AppPhoto;
import org.exchanger_bot.model.BinaryContent;
import org.exchanger_bot.repository.AppDocumentRepository;
import org.exchanger_bot.repository.AppPhotoRepository;
import org.exchanger_bot.repository.BinaryContentRepository;
import org.exchanger_bot.service.FileDownloader;
import org.exchanger_bot.service.FileService;
import org.exchanger_bot.service.enums.LinkType;
import org.exchanger_bot.service.exceptions.UploadFileException;
import org.exchanger_bot.utils.CryptoTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


public class FileServiceImplTest {


    private static final String RESPONSE_JSON = "{"
            + "\"result\": {"
            + "\"file_path\": \"path/to/your/file\""
            + "}"
            + "}";
    public static final String RESPONSE_ERROR_MESSAGE = "Error";
    public static final String FILE_ID = "fileId";
    public static final String URL = "url";
    public static final String TOKEN = "token";
    private static final byte[] byteArray = new byte[0];
    private static final BinaryContent binaryContent = new BinaryContent();
    public static final int INDEX = 0;
    public static final int ID = 0;
    public static final String HASH = "hash";
    public static final String LINK_ADDRESS = "linkAddress";
    public static final String GENERATED_LINK = "http://linkAddress/files/get-photo?id=hash";

    @InjectMocks
    private FileServiceImpl fileService;




    @Mock
    private Message message;
    @Mock
    private Document document;


    @Mock
    private List<PhotoSize> photoList;
    @Mock
    private PhotoSize photo;
    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private AppDocumentRepository appDocumentRepository;

    @Mock
    private AppPhotoRepository appPhotoRepository;

    @Mock
    private CryptoTools cryptoTools;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FileDownloader fileDownloader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(message.getDocument()).thenReturn(document);
        when(document.getFileId()).thenReturn(FILE_ID);
        when(message.getPhoto()).thenReturn(photoList);
        when(photoList.size()).thenReturn(INDEX);
        when(photoList.get(INDEX)).thenReturn(photo);
        when(photo.getFileId()).thenReturn(FILE_ID);

        HttpMethod httpMethod = HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        when(restTemplate.exchange(
                anyString(),
                eq(httpMethod),
                eq(request),
                eq(String.class),
                anyString(),
                anyString()
        )).thenReturn(ResponseEntity.ok(RESPONSE_JSON));

        when(fileDownloader.downloadFile(anyString())).thenReturn(byteArray);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);
        when(appDocumentRepository.save(any(AppDocument.class))).thenReturn(mock(AppDocument.class));
        when(appPhotoRepository.save(any(AppPhoto.class))).thenReturn(mock(AppPhoto.class));
        when(cryptoTools.hashOf(ID)).thenReturn(HASH);




    }


    @Test
    public void testProcessDoc_ReturnsAppDocument() {
        ReflectionTestUtils.setField(fileService, "fileInfoUri", URL);
        ReflectionTestUtils.setField(fileService, "token", TOKEN);
        AppDocument result = fileService.processDoc(message);

        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
        verify(appDocumentRepository, times(1)).save(any(AppDocument.class));
        assertThat(result).isInstanceOf(AppDocument.class);
    }


    @Test
    public void testProcessDoc_RestTemplateReturnsNonOkStatus_ThrowException() {
        ReflectionTestUtils.setField(fileService, "fileInfoUri", URL);
        ReflectionTestUtils.setField(fileService, "token", TOKEN);
        HttpMethod httpMethod = HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        when(restTemplate.exchange(
                anyString(),
                eq(httpMethod),
                eq(request),
                eq(String.class),
                anyString(),
                anyString()
        )).thenReturn(ResponseEntity.badRequest().body(RESPONSE_ERROR_MESSAGE));

        assertThatThrownBy(() -> fileService.processDoc(message)).isInstanceOf(UploadFileException.class);
    }



    @Test
    public void testProcessPhoto_ReturnsAppPhoto() {
        ReflectionTestUtils.setField(fileService, "fileInfoUri", URL);
        ReflectionTestUtils.setField(fileService, "token", TOKEN);
        AppPhoto result = fileService.processPhoto(message);

        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
        verify(appPhotoRepository, times(1)).save(any(AppPhoto.class));
        assertThat(result).isInstanceOf(AppPhoto.class);
    }


    @Test
    public void testProcessPhoto_RestTemplateReturnsNonOkStatus_ThrowException() {
        ReflectionTestUtils.setField(fileService, "fileInfoUri", URL);
        ReflectionTestUtils.setField(fileService, "token", TOKEN);
        HttpMethod httpMethod = HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        when(restTemplate.exchange(
                anyString(),
                eq(httpMethod),
                eq(request),
                eq(String.class),
                anyString(),
                anyString()
        )).thenReturn(ResponseEntity.badRequest().body(RESPONSE_ERROR_MESSAGE));

        assertThatThrownBy(() -> fileService.processPhoto(message)).isInstanceOf(UploadFileException.class);
    }


    @Test
    public void generateDownloadLink_ReturnsLink() {
        ReflectionTestUtils.setField(fileService, "linkAddress", LINK_ADDRESS);
        assertThat(fileService.generateDownloadLink(ID,LinkType.GET_PHOTO )).isEqualTo(GENERATED_LINK);
    }










}
