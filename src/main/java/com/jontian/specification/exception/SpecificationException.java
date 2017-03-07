package com.jontian.specification.exception;

public class SpecificationException extends Exception {
    public SpecificationException() {
    }

    public SpecificationException(String string) {
        super(string);
    }

    public SpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecificationException(Throwable cause) {
        super(cause);
    }

}
