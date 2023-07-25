package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;

@Entity
public class ReservedSeat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private int seatNumber;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "screening_id")
    private ScreeningTime screeningTime;

    public ReservedSeat() {
    }

    public ReservedSeat(int seatNumber, Room room, Reservation reservation, ScreeningTime screeningTime) {
        this.seatNumber = seatNumber;
        this.room = room;
        this.reservation = reservation;
        this.screeningTime = screeningTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ScreeningTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(ScreeningTime screeningTime) {
        this.screeningTime = screeningTime;
    }
}
