package com.cinema.model;

import java.util.List;

public record TemporalSeatList(Long reservationId, List<Integer> reservedSeats) {
    public TemporalSeatList(Long reservationId, List<Integer> reservedSeats) {
        this.reservationId = reservationId;
        this.reservedSeats = reservedSeats;
    }
}
