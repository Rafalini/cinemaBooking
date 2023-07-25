package com.cinema.model;

//Record representing data model sent from frontend to create Reservation (id's instead references)
public record TemporalReservation(String name, String surname, String value, Long screeningTimeId) {

    public TemporalReservation(String name, String surname, String value, Long screeningTimeId) {
        this.name = name;
        this.surname = surname;
        this.value = value;
        this.screeningTimeId = screeningTimeId;
    }
}
