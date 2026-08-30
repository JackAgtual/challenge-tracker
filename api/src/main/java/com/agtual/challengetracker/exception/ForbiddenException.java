package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class ForbiddenException extends ApplicationException {

    public static final String TITLE = "Forbidden Operation";

    public ForbiddenException(ResourceType resourceType, Object identifier) {
        super(TITLE, resourceType, identifier);
    }

    public ForbiddenException(ResourceType resourceType, Object identifier, String msg) {
        super(TITLE, resourceType, identifier, msg);
    }

    public ForbiddenException(String msg) {
        super(TITLE, msg);
    }
}
