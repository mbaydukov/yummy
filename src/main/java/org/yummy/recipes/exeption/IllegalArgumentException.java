package org.yummy.recipes.exeption;


public class IllegalArgumentException extends RuntimeException {

    public IllegalArgumentException() {
    }

    public IllegalArgumentException(String s) {
        super(s);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public IllegalArgumentException(String s, Object...args) {
        super(String.format(s, args));
    }
}
