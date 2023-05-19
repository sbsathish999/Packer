package com.mobiquity.packer.exception;

public class APIException extends Throwable{
    public APIException(String message) {
        super(message);
    }
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
