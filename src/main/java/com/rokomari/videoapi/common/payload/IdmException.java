package com.rokomari.videoapi.common.payload;

public class IdmException extends Throwable {
    public IdmException(String message) {
        super(message);
    }

    public IdmException(Throwable t) {
        super(t);
    }

    public IdmException(String message, Throwable t) {
        super(message, t);
    }
}
