package com.jahia.elasticsearch.sample.exceptions;

/**
 * Generic system exception
 */
public class SystemProcessException extends RuntimeException {
    public SystemProcessException() {
        super();
    }

    public SystemProcessException(String message) {
        super(message);
    }

    public SystemProcessException(Throwable cause) {
        super(cause);
    }

    public SystemProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
