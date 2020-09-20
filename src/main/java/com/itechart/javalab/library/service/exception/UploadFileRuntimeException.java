package com.itechart.javalab.library.service.exception;

public class UploadFileRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5930724532935831886L;

    public UploadFileRuntimeException() {
        super();
    }

    public UploadFileRuntimeException(String message) {
        super(message);
    }

    public UploadFileRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileRuntimeException(Throwable cause) {
        super(cause);
    }
}
