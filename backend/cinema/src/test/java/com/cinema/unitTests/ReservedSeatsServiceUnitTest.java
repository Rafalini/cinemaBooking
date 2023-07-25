package com.cinema.unitTests;

import com.cinema.exceptions.ReservedSeatNotFoundException;
import com.cinema.model.Reservation;
import com.cinema.model.ReservedSeat;
import com.cinema.model.ScreeningTime;
import com.cinema.repository.ReservedSeatsRepo;
import com.cinema.service.ReservedSeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class ReservedSeatsServiceUnitTest {
    @Mock
    private ReservedSeatsRepo reservedSeatsRepo;
    @InjectMocks
    private ReservedSeatService reservedSeatService;

    @BeforeEach
    void setup() {
        reservedSeatsRepo = Mockito.mock(ReservedSeatsRepo.class);
        reservedSeatService = new ReservedSeatService(reservedSeatsRepo);
    }

    @Test
    void addTest() {
        ReservedSeat reservedSeat = new ReservedSeat();
        reservedSeat.setSeatNumber(5);

        when(reservedSeatsRepo.save(reservedSeat)).thenReturn(reservedSeat);
        ReservedSeat returnedReservedSeat = this.reservedSeatService.addReservedSeat(reservedSeat);

        assertEquals(reservedSeat, returnedReservedSeat);
        verify(this.reservedSeatsRepo).save(reservedSeat);
    }
    
    @Test
    void findAllTest() {
        ReservedSeat reservedSeat = new ReservedSeat();
        reservedSeat.setSeatNumber(5);

        when(reservedSeatsRepo.findAll()).thenReturn(List.of(reservedSeat));
        List<ReservedSeat> reservedSeats = this.reservedSeatService.findAllReservedSeats();

        assertEquals(List.of(reservedSeat), reservedSeats);
        verify(this.reservedSeatsRepo).findAll();
    }

    @Test
    void updateTest() {
        ReservedSeat reservedSeat = new ReservedSeat();
        reservedSeat.setSeatNumber(5);

        when(reservedSeatsRepo.save(reservedSeat)).thenReturn(reservedSeat);
        ReservedSeat returnedReservedSeat = this.reservedSeatService.addReservedSeat(reservedSeat);
        reservedSeat.setSeatNumber(9);
        ReservedSeat returnedReservedSeat2 = this.reservedSeatService.updateReservedSeat(returnedReservedSeat);
        assertEquals(returnedReservedSeat, returnedReservedSeat2);
        verify(this.reservedSeatsRepo, times(2)).save(reservedSeat);
    }

    @Test
    void findByScreeningTime() {
        ReservedSeat reservedSeat = new ReservedSeat();
        ScreeningTime screeningTime = new ScreeningTime();
        screeningTime.setId(2L);
        reservedSeat.setSeatNumber(5);
        reservedSeat.setScreeningTime(screeningTime);

        when(reservedSeatsRepo.findReservedSeatByScreeningTimeId(2L)).thenReturn(Optional.of(List.of(reservedSeat)));
        when(reservedSeatsRepo.findReservedSeatByScreeningTimeId(404L)).thenThrow(new ReservedSeatNotFoundException("Message"));
        List<ReservedSeat> reservedSeats = this.reservedSeatService.findReservedSeatByScreeningTime(2L);

        assertEquals(List.of(reservedSeat), reservedSeats);
        assertThrows(ReservedSeatNotFoundException.class , () -> {this.reservedSeatService.findReservedSeatByScreeningTime(404L);});

        verify(this.reservedSeatsRepo).findReservedSeatByScreeningTimeId(2L);
        verify(this.reservedSeatsRepo).findReservedSeatByScreeningTimeId(404L);
    }
    @Test
    void findByReservationId() {
        ReservedSeat reservedSeat = new ReservedSeat();
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservedSeat.setSeatNumber(5);
        reservedSeat.setReservation(reservation);

        when(reservedSeatsRepo.findReservedSeatByReservationId(1L)).thenReturn(Optional.of(List.of(reservedSeat)));
        when(reservedSeatsRepo.findReservedSeatByReservationId(404L)).thenThrow(new ReservedSeatNotFoundException("Message"));
        List<ReservedSeat> reservedSeats = this.reservedSeatService.findReservedSeatByReservationId(1L);

        assertEquals(List.of(reservedSeat), reservedSeats);
        assertThrows(ReservedSeatNotFoundException.class , () -> {this.reservedSeatService.findReservedSeatByReservationId(404L);});

        verify(this.reservedSeatsRepo).findReservedSeatByReservationId(1L);
        verify(this.reservedSeatsRepo).findReservedSeatByReservationId(404L);
    }
}
