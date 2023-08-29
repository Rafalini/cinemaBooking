package com.cinema.service;

import com.cinema.exceptions.ReservationNotFoundException;
import com.cinema.exceptions.ScreeningTimeNotFoundException;
import com.cinema.model.*;
import com.cinema.repository.ReservationRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
public class ReservationService {
    @Value("${reservation.minutes.to.delete}")
    private int minutesToDelete;
    @Value("${reservation.minutes.from.screening.to.delete}")
    private int minutesFromScreeningToDelete;
    private final ReservationRepo reservationRepo;
    private final ReservedSeatService reservedSeatService;
    private final ScreeningService screeningService;

    @Autowired
    public ReservationService(ReservationRepo reservationRepo, ReservedSeatService reservedSeatService, ScreeningService screeningService){

        this.reservationRepo = reservationRepo;
        this.reservedSeatService = reservedSeatService;
        this.screeningService = screeningService;
    }

    public Reservation addReservation(TemporalReservation reservation){
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
        return reservationRepo.save(newReservation);
    }

    public List<ReservationDetails> findReservationsAndParse(){

        List<ReservationDetails> details = new ArrayList<>();

        for(Reservation reservation:reservationRepo.findAll()){
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

        return details;
    }

    public Reservation findReservationById(Long id){
        return reservationRepo.findReservationById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation "+id+" was not found"));
    }
    public void deleteReservation(Long id){
        reservationRepo.deleteReservationById(id);
    }

    public boolean verifyReservation(Long id, int code){

        Reservation reservation = this.findReservationById(id);
        if(reservation.getVerificationCode() == code){
            reservation.setConfirmed(true);
            reservation.setVerificationCode(null);
            reservationRepo.save(reservation);
            return true;
        }
        return false;
    }

    public List<Reservation> findAllUnconfirmedReservations(boolean confirmed){
        return reservationRepo.findReservationByConfirmed(confirmed)
                .orElseThrow(() -> new ReservationNotFoundException("No unconfirmed reservations found for deletion"));
    }

    public List<ReservedSeat> addReservation(TemporalSeatList seatList){
        Reservation reservation = this.findReservationById(seatList.reservationId());
        List<ReservedSeat> newList = new ArrayList<>();
        for(Integer seat : seatList.reservedSeats()){
            ReservedSeat reservedSeat = new ReservedSeat();
            reservedSeat.setReservation(reservation);
            reservedSeat.setSeatNumber(seat);
            reservedSeat.setScreeningTime(reservation.getScreeningTime());
            reservedSeat.setRoom(reservation.getScreeningTime().getRoom());
            newList.add(reservedSeatService.addReservedSeat(reservedSeat));
        }
        return newList;
    }
    //Method runs every minute, checks for reservations that aren't confirmed and deletes them if it is time to do so.
    //It isn't placed in service, because it refers to other service (reservedSeatService), because there is one direction
    //reference from reserved seat to screening and cascade deletion on reservation simply won't work.
    @Scheduled(cron = "*/60 * * * * *")
    @Transactional
    public void deleteInactive(){
        List<Reservation> reservations = this.findAllUnconfirmedReservations(false);
        for(Reservation reservation : reservations){
            Duration duration = Duration.between(reservation.getReservationTime(), LocalDateTime.now());
            Duration timeToScreening = Duration.between(LocalDateTime.now(), reservation.getScreeningTime().getScreeningTime());

            if(duration.toSeconds() >= minutesToDelete * 60L || timeToScreening.toSeconds() <= minutesFromScreeningToDelete * 60L){
                // Association from seats to reservation -> CASCADE DELETE won't work here
                List<ReservedSeat> seats = this.reservedSeatService.findReservedSeatByReservationId(reservation.getId());
                for(ReservedSeat seat : seats)
                    this.reservedSeatService.deleteReservedSeat(seat.getId());
                this.deleteReservation(reservation.getId());
            }
        }
    }
}
