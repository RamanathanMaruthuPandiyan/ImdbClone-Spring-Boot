package com.imdbclone.imdbclone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalApiException extends RuntimeException{
    public ExternalApiException(String message) {
        super(message);
    }
}
