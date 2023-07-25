package com.cinema.model;

//Record representing reservation sent to frontend
public record ReservationDetails(Long id, String name, String surname, String title, String time, String value, String url, int seats) {
    public ReservationDetails(Long id, String name, String surname, String title, String time, String value, String url, int seats) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.title = title;
        this.time = time;
        this.value = value;
        this.url=url;
        this.seats = seats;
    }
}
