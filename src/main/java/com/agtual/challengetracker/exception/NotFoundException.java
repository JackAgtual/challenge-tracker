package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ResourceType resourceType, Object identifier) {
        super("not found", resourceType, identifier);
    }
}
