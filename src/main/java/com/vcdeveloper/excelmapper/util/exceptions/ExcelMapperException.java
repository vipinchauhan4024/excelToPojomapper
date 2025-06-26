package com.vcdeveloper.excelmapper.util.exceptions;

public class ExcelMapperException extends RuntimeException {

    private static final long serialVersionUID = 1;

    /**
     * Application specific exception to be thrown If some Exception occure in Excel mapping.
     * @param messageKey - A specific message to provide when the exception is thrown
     */
    public ExcelMapperException(String messageKey) {
        super(messageKey);
    }

    /**
     * Application specific exception to be thrown If some Exception occure in Excel mapping.
     * @param messageKey - A specific message to provide when the exception is thrown
     * @param cause - Cause of Exception
     */
    public ExcelMapperException(String messageKey, Throwable cause) {
        super(messageKey);
    }
}