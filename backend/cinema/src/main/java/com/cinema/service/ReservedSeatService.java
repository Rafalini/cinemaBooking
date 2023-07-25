package com.cinema.service;

import com.cinema.exceptions.ReservedSeatNotFoundException;
import com.cinema.model.ReservedSeat;
import com.cinema.repository.ReservedSeatsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservedSeatService {

    private final ReservedSeatsRepo reservedSeatsRepo;
    @Autowired
    public ReservedSeatService(ReservedSeatsRepo reservedSeatsRepo) {
        this.reservedSeatsRepo = reservedSeatsRepo;
    }

    public ReservedSeat addReservedSeat(ReservedSeat reservedSeat){
        return reservedSeatsRepo.save(reservedSeat);
    }

    public ReservedSeat updateReservedSeat(ReservedSeat reservedSeat){
        return reservedSeatsRepo.save(reservedSeat);
    }

    public void deleteReservedSeat(Long id){
        reservedSeatsRepo.deleteReservedSeatById(id);
    }
    public List<ReservedSeat> findAllReservedSeats(){
        return reservedSeatsRepo.findAll();
    }

    public List<ReservedSeat> findReservedSeatByScreeningTime(Long id){
        return reservedSeatsRepo.findReservedSeatByScreeningTimeId(id)
                .orElseThrow(() -> new ReservedSeatNotFoundException("Reserved seats for screening "+id+" were not found"));
    }

    public List<ReservedSeat> findReservedSeatByReservationId(Long id){
        return reservedSeatsRepo.findReservedSeatByReservationId(id)
                .orElseThrow(() -> new ReservedSeatNotFoundException("Reserved seats for screening "+id+" were not found"));
    }
}
