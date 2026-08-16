package com.agtual.challengetracker.exception;

import com.agtual.challengetracker.enums.ResourceType;

public abstract class ApplicationException extends RuntimeException {
    protected ApplicationException(String message) {
        super(message);
    }

    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ApplicationException(String errorType, ResourceType resourceType, Object identifier) {
        super(resourceType.getDisplayName() + " " + errorType + ": " + identifier);
    }

}
