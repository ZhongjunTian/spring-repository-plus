package com.jontian.specification.exception;

/**
 * Created by jtian on 11/10/2016.
 */
public class GenericFilterException extends Exception {
    public GenericFilterException() {
    }

    public GenericFilterException(String string) {
        super(string);
    }

    public GenericFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericFilterException(Throwable cause) {
        super(cause);
    }

}
