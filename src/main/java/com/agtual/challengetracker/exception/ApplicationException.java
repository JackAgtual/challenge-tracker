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

    protected ApplicationException(String errorType, ResourceType resourceType, Object identifier, String msg) {
        super(resourceType.getDisplayName() + " " + errorType + ": " + identifier + ". " + msg);
    }

    protected ApplicationException(String errorType, ResourceType resourceType, String field, Object value) {
        super(resourceType.getDisplayName() + " " + errorType + ": " + field + "=" + value);
    }

}
