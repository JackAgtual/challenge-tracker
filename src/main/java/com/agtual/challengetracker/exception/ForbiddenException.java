package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class ForbiddenException extends ApplicationException {

    private static final String ERROR_TYPE = "Forbidden operation";

    public ForbiddenException(ResourceType resourceType, Object identifier) {
        super(ERROR_TYPE, resourceType, identifier);
    }

    public ForbiddenException(ResourceType resourceType, Object identifier, String msg) {
        super(ERROR_TYPE, resourceType, identifier, msg);
    }

    public ForbiddenException(String msg) {
        super(ERROR_TYPE, msg);
    }
}
