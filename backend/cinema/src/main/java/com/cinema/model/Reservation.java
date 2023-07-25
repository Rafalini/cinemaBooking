package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    private String surname;

    private BigDecimal value;

    @Column(nullable = true)
    private Integer verificationCode;

    private boolean confirmed;
//    @ManyToOne(cascade = CascadeType.REMOVE)
    @ManyToOne
    @JoinColumn(name = "screening_id")
    private ScreeningTime screeningTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime reservationTime;
    public Reservation(){}
    public Reservation(String name, String surname, BigDecimal value, Boolean confirmed, ScreeningTime screening, LocalDateTime reservationTime) {
        this.name = name;
        this.surname = surname;
        this.value = value;
        this.confirmed = confirmed;
        this.screeningTime = screening;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public ScreeningTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(ScreeningTime screening) {
        this.screeningTime = screening;
    }

    public BigDecimal getValue() { return value; }

    public void setValue(BigDecimal value) { this.value = value; }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getVerificationCode() {
        return (verificationCode==null)? -1 : verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
