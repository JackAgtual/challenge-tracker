package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class AlreadyExistsException extends ApplicationException {

    public static final String TITLE = "Already Exists";

    public AlreadyExistsException(ResourceType resourceType, Object identifier) {
        super(TITLE, resourceType, identifier);
    }
}
