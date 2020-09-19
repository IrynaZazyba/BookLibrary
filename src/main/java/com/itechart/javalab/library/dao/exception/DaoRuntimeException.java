package com.itechart.javalab.library.dao.exception;

public class DaoRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3194358343606106412L;

    public DaoRuntimeException() {
        super();
    }

    public DaoRuntimeException(String message) {
        super(message);
    }

    public DaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoRuntimeException(Throwable cause) {
        super(cause);
    }
}
