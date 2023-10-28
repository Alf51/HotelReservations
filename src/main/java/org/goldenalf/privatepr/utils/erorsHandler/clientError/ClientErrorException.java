package org.goldenalf.privatepr.utils.erorsHandler.clientError;

public class ClientErrorException extends RuntimeException {
    public ClientErrorException(String message) {
        super(message);
    }
}
