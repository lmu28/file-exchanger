package org.exchanger_bot.service.exceptions;

public class UploadFileException extends RuntimeException{

    public UploadFileException() {
    }

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileException(Throwable cause) {
        super(cause);
    }
}
