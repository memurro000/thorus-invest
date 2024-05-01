package com.murro.inv.bybit.api.util;

public class BybitWSAPIException extends RuntimeException{
    public BybitWSAPIException(String message) {
        super(message);
    }

    public BybitWSAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
