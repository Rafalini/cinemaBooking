package com.cinema.model;

import java.util.List;
//Record representing seat list included in reservation model sent from frontend to create Reservation (id's instead references)

public record TemporalSeatList(Long reservationId, List<Integer> reservedSeats) {
    public TemporalSeatList(Long reservationId, List<Integer> reservedSeats) {
        this.reservationId = reservationId;
        this.reservedSeats = reservedSeats;
    }
}
