package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class AlreadyExistsException extends ApplicationException {

    public AlreadyExistsException(ResourceType resourceType, Object identifier) {
        super("already exists", resourceType, identifier);
    }
}
