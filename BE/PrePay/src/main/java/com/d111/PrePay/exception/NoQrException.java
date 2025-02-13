package com.d111.PrePay.exception;

import java.util.NoSuchElementException;

public class NoQrException extends NoSuchElementException {
    public NoQrException(String message) {
        super(message);
    }
}
