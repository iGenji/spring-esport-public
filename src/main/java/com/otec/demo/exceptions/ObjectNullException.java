package com.otec.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectNullException extends RuntimeException {
    public ObjectNullException(String message) {
        super(message);
    }

    public ObjectNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
