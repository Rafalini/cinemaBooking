package com.cinema.service;

import com.cinema.exceptions.CustomNotFoundException;
import com.cinema.exceptions.ReservationNotFoundException;
import com.cinema.model.Reservation;
import com.cinema.repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReservationService {
    private final ReservationRepo reservationRepo;

    @Autowired
    public ReservationService(ReservationRepo reservationRepo){
        this.reservationRepo = reservationRepo;
    }

    public Reservation addReservation(Reservation reservation){
        return reservationRepo.save(reservation);
    }

    public List<Reservation> findAllReservations(){
        return reservationRepo.findAll();
    }

    public Reservation findReservationById(Long id){
        return reservationRepo.findReservationById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation "+id+" was not found"));
    }
    public void deleteReservation(Long id){
        reservationRepo.deleteReservationById(id);
    }

    public Reservation updateReservation(Reservation reservation){
        return reservationRepo.save(reservation);
    }

    public List<Reservation> findAllUnconfirmedReservations(boolean confirmed){
        return reservationRepo.findReservationByConfirmed(confirmed)
                .orElseThrow(() -> new ReservationNotFoundException("No unconfirmed reservations found for deletion"));
    }
}
