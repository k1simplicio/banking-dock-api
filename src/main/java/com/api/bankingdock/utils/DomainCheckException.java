package com.api.bankingdock.utils;

public class DomainCheckException extends IllegalArgumentException {

    DomainCheckException(String message) {
        super(message);
    }
}
