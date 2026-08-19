package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class ForbiddenException extends ApplicationException {

    public ForbiddenException(ResourceType resourceType, Object identifier) {
        super("forbidden operation", resourceType, identifier);
    }

    public ForbiddenException(ResourceType resourceType, Object identifier, String msg) {
        super("forbidden operation", resourceType, identifier, msg);
    }
}
