package com.jahia.elasticsearch.sample.exceptions.elasticsearch;

/**
 * Exception defined for
 */
public class UndefiinedEsClientException extends Exception {
    public UndefiinedEsClientException() {
        super();
    }

    public UndefiinedEsClientException(String message) {
        super(message);
    }

    protected UndefiinedEsClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
