package com.agtual.challengetracker.exception;

import org.springframework.http.HttpStatus;

import com.agtual.challengetracker.enums.ResourceType;

public class AlreadyExistsException extends ApplicationException {

    public static final String TITLE = "Conflict — resource already exists";
    public static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public AlreadyExistsException(ResourceType resourceType, Object identifier) {
        super(TITLE, resourceType, identifier);
    }
}
