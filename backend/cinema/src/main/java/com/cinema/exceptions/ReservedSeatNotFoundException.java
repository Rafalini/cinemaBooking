package com.cinema.exceptions;

public class ReservedSeatNotFoundException extends RuntimeException{
    public ReservedSeatNotFoundException(String message) {
        super(message);
    }
}
