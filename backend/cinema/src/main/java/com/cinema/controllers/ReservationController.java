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

    @Value("${reservation.minutes.to.delete}")
    private int minutesToDelete;
    @Value("${reservation.minutes.from.screening.to.delete}")
    private int minutesFromScreeningToDelete;
    private final ReservationService reservationService;
    private final ScreeningService screeningService;
    private final ReservedSeatService reservedSeatService;
    public ReservationController(ReservationService reservationservice, ScreeningService screeningService, ReservedSeatService reservedSeatService) {
        this.reservationService = reservationservice;
        this.screeningService = screeningService;
        this.reservedSeatService = reservedSeatService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDetails>> getAllReservations(){
        List<Reservation> reservations = reservationService.findAllReservations();
        List<ReservationDetails> details = new ArrayList<>();

        for(Reservation reservation:reservations){
            List<ReservedSeat> rooms = this.reservedSeatService.findReservedSeatByReservationId(reservation.getId());
            String urlPath = null;
            if(!reservation.isConfirmed())
                urlPath = "/verify/"+reservation.getId()+"/"+ reservation.getVerificationCode();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            ReservationDetails detailed = new ReservationDetails(
                    reservation.getId(),
                    reservation.getName(),
                    reservation.getSurname(),
                    reservation.getScreeningTime().getMovie().getTitle(),
                    reservation.getScreeningTime().getScreeningTime().format(timeFormatter),
                    reservation.getValue().toPlainString(),
                    urlPath,
                    rooms.size()
            );

            details.add(detailed);
        }

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id){
        Reservation reservation = reservationService.findReservationById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody TemporalReservation reservation){
        Reservation newReservation = new Reservation();
        newReservation.setName(reservation.name());
        newReservation.setSurname(reservation.surname());
        newReservation.setValue(new BigDecimal(reservation.value()));
        newReservation.setVerificationCode(newReservation.hashCode());
        newReservation.setReservationTime(LocalDateTime.now());
        newReservation.setConfirmed(false);

        try {
            newReservation.setScreeningTime(screeningService.findScreeningTimeById(reservation.screeningTimeId()));
        } catch (ScreeningTimeNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Screening ID Not Found", ex);
        }
        Reservation readyReservation = reservationService.addReservation(newReservation);
        return new ResponseEntity<>(readyReservation, HttpStatus.CREATED);
    }

    @Scheduled(cron = "*/60 * * * * *")
    @Transactional
    public void deleteInactive(){
        List<Reservation> reservations = this.reservationService.findAllUnconfirmedReservations(false);
        for(Reservation reservation : reservations){
            Duration duration = Duration.between(reservation.getReservationTime(), LocalDateTime.now());
            Duration timeToScreening = Duration.between(LocalDateTime.now(), reservation.getScreeningTime().getScreeningTime());

            if(duration.toSeconds() >= minutesToDelete * 60L || timeToScreening.toSeconds() <= minutesFromScreeningToDelete * 60L){
                // Association from seats to reservation -> CASCADE DELETE won't work here
                List<ReservedSeat> seats = this.reservedSeatService.findReservedSeatByReservationId(reservation.getId());
                for(ReservedSeat seat : seats)
                    this.reservedSeatService.deleteReservedSeat(seat.getId());
                this.reservationService.deleteReservation(reservation.getId());
            }
        }
    }

    @GetMapping("/verify/{id}/{code}")
    public ResponseEntity<?> verifyReservation(@PathVariable Long id, @PathVariable int code){
        try {
            Reservation reservation = reservationService.findReservationById(id);
            if(reservation.getVerificationCode() == code){
                reservation.setConfirmed(true);
                reservation.setVerificationCode(null);
                reservationService.updateReservation(reservation);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (ScreeningTimeNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Reservation ID Not Found", ex);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/seats/{id}")
    public ResponseEntity<List<ReservedSeat>> getReservedSeats(@PathVariable("id") Long screeningId){
        List<ReservedSeat> seats = reservedSeatService.findReservedSeatByScreeningTime(screeningId);
        seats.sort(Comparator.comparing(ReservedSeat::getSeatNumber));
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

    @PostMapping("/add-seats/")
    public ResponseEntity<List<ReservedSeat>> addReservation(@RequestBody TemporalSeatList seatList){
        Reservation reservation = reservationService.findReservationById(seatList.reservationId());
        List<ReservedSeat> newList = new ArrayList<>();
        for(Integer seat : seatList.reservedSeats()){
            ReservedSeat reservedSeat = new ReservedSeat();
            reservedSeat.setReservation(reservation);
            reservedSeat.setSeatNumber(seat);
            reservedSeat.setScreeningTime(reservation.getScreeningTime());
            reservedSeat.setRoom(reservation.getScreeningTime().getRoom());
            newList.add(reservedSeatService.addReservedSeat(reservedSeat));
        }
        return new ResponseEntity<>(newList, HttpStatus.CREATED);
    }
}
