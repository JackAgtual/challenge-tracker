package com.agtual.challengetracker.exception;

import org.springframework.http.HttpStatus;

import com.agtual.challengetracker.enums.ResourceType;

public class NotFoundException extends ApplicationException {

    public static final String TITLE = "Not Found";
    public static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(ResourceType resourceType, Object identifier) {
        super(TITLE, resourceType, identifier);
    }

    public NotFoundException(ResourceType resourceType, String field, Object value) {
        super(TITLE, resourceType, field, value);
    }
}
