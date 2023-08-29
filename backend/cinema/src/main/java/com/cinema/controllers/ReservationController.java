package com.cinema.controllers;

import com.cinema.exceptions.ScreeningTimeNotFoundException;
import com.cinema.model.*;
import com.cinema.service.ReservationService;
import com.cinema.service.ReservedSeatService;
import com.cinema.service.ScreeningService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import  java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@EnableScheduling
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservedSeatService reservedSeatService;
    public ReservationController(ReservationService reservationservice, ReservedSeatService reservedSeatService) {
        this.reservationService = reservationservice;
        this.reservedSeatService = reservedSeatService;
    }

    @GetMapping("/all")
    public List<ReservationDetails> getAllReservations(){
        return reservationService.findReservationsAndParse();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable("id") Long id){
        return reservationService.findReservationById(id);
    }

    @PostMapping("/add")
    public Reservation addReservation(@RequestBody TemporalReservation reservation){
        return reservationService.addReservation(reservation);
    }

    @GetMapping("/verify/{id}/{code}")
    public ResponseEntity<?> verifyReservation(@PathVariable Long id, @PathVariable int code){
        if(this.reservationService.verifyReservation(id, code))
            return new ResponseEntity<>( HttpStatus.OK);
        else
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/seats/{id}")
    public List<ReservedSeat> getReservedSeats(@PathVariable("id") Long screeningId){
        return reservedSeatService.findReservedSeatByScreeningTime(screeningId);
    }


    @PostMapping("/add-seats/")
    public List<ReservedSeat> addReservation(@RequestBody TemporalSeatList seatList){
        return reservationService.addReservation(seatList);
    }
}
