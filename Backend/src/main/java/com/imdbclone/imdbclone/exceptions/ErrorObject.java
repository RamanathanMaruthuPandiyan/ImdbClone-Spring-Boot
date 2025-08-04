package com.imdbclone.imdbclone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorObject {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int    status;
    private final String error;
    private final List<String> message;
    private final String path;
    private final String exceptionMessage;
}
