package org.exchanger_bot.service.impl;

import org.exchanger_bot.service.exceptions.UploadFileException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileDownloaderImplTest {


    public static final String TEST_URL = "http://localhost/";
    public static final String FILE_STORAGE_URI = "https://api.telegram.org/file/bottoken/filePath";
    public static final String PROTOCOL = "http";

    abstract public class AbstractPublicStreamHandler extends URLStreamHandler { // чтобы не возвращать реальный Connection(будет возвращать mock-connection)
        @Override
        public URLConnection openConnection(URL url) throws IOException {
            return null;
        }
    }


    private static final byte[] BYTES = "test_data".getBytes();
    private static final InputStream STREAM = new ByteArrayInputStream(BYTES);
    public static final String FILE_PATH = "filePath";
    @Spy
    private FileDownloaderImpl fileDownloader;
    @Mock
    private  URLStreamHandlerFactory urlStreamHandlerFactory;
    @Mock
    private URLConnection urlConnection;
    @Mock
    private AbstractPublicStreamHandler publicStreamHandler;

    private static URL mockedUrl;

    //Невозможно замокать URL - так как он final
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(fileDownloader,"fileStorageUri", FILE_STORAGE_URI);
        ReflectionTestUtils.setField(fileDownloader,"token","token");
    }

    @Test
    void downloadFile() throws IOException {
        // устанавливаем свою фабрику
        URL.setURLStreamHandlerFactory(urlStreamHandlerFactory);
        when(urlStreamHandlerFactory.createURLStreamHandler(PROTOCOL)).thenReturn(publicStreamHandler);
        when(publicStreamHandler.openConnection(any(URL.class))).thenReturn(urlConnection);
        when(urlConnection.getInputStream()).thenReturn(STREAM);
        // Со Spy нужно использовать doReturn
        mockedUrl = new URL(TEST_URL);
        doReturn(mockedUrl).when(fileDownloader).createUrl(anyString());

        byte[] result = fileDownloader.downloadFile(FILE_PATH);
        assertThat(result).isEqualTo(BYTES);
    }



    @Test
    void downloadFile_MalformedURL_ThrowException() throws MalformedURLException {
        doThrow(new MalformedURLException()).when(fileDownloader).createUrl(anyString());

        assertThatThrownBy( () -> fileDownloader.downloadFile(FILE_PATH)).isInstanceOf(UploadFileException.class);
    }



    @Test
    void downloadFile_ErrorOpenStream_ThrowException() throws IOException {
        doThrow(new IOException()).when(urlConnection).getInputStream();

       assertThatThrownBy( () -> fileDownloader.downloadFile(FILE_PATH)).isInstanceOf(UploadFileException.class);
    }
}





