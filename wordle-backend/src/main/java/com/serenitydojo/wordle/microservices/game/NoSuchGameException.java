package com.serenitydojo.wordle.microservices.game;

public class NoSuchGameException extends RuntimeException {
    public NoSuchGameException(String message) {
        super(message);
    }
}
