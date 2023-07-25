package com.cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;

//Class representing room, it has only information about seats.
//There is assumption that all rooms are rectangles (there are no breaks in rows or collumns).
@Entity
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private int seatRows;
    private int seatsInRow;

    public Room(){}
    public Room(int seatRows, int seatsInRow) {
        this.seatRows = seatRows;
        this.seatsInRow = seatsInRow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){this.id = id;}

    public int getSeatRows() {
        return seatRows;
    }

    public void setSeatRows(int rows) {
        this.seatRows = rows;
    }

    public int getSeatsInRow() {
        return seatsInRow;
    }

    public void setSeatsInRow(int seatsInRow) {
        this.seatsInRow = seatsInRow;
    }
}
