package com.cinema.repository;

import com.cinema.model.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservedSeatsRepo extends JpaRepository<ReservedSeat, Long> {
    Optional<List<ReservedSeat>> findReservedSeatByScreeningTimeId(Long id);
    Optional<List<ReservedSeat>> findReservedSeatByReservationId(Long id);

    void deleteReservedSeatById(Long id);
}
