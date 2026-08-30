package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class AlreadyExistsException extends ApplicationException {

    public static final String TITLE = "Conflict — resource already exists";

    public AlreadyExistsException(ResourceType resourceType, Object identifier) {
        super(TITLE, resourceType, identifier);
    }
}
