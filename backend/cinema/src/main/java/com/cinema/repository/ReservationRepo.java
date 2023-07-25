package com.cinema.repository;

import com.cinema.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    void deleteReservationById(Long id);

    Optional<Reservation> findReservationById(Long id);

    Optional<List<Reservation>> findReservationByConfirmed(boolean confirmed);
}
