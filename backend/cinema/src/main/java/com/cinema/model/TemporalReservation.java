package com.cinema.model;

public record TemporalReservation(String name, String surname, String value, Long screeningTimeId) {
    public TemporalReservation(String name, String surname, String value, Long screeningTimeId) {
        this.name = name;
        this.surname = surname;
        this.value = value;
        this.screeningTimeId = screeningTimeId;
    }
}
