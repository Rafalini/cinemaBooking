package com.cinema.exceptions;

public class ScreeningTimeNotFoundException extends RuntimeException{
    public ScreeningTimeNotFoundException(String message) {
        super(message);
    }
}
