package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class NotFoundException extends ApplicationException {

    private static final String ERROR_TYPE = "not found";

    public NotFoundException(ResourceType resourceType, Object identifier) {
        super(ERROR_TYPE, resourceType, identifier);
    }

    public NotFoundException(ResourceType resourceType, String field, Object value) {
        super(ERROR_TYPE, resourceType, field, value);
    }
}
