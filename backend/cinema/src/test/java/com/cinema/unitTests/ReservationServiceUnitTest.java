package com.cinema.unitTests;

import com.cinema.exceptions.ReservationNotFoundException;
import com.cinema.model.Reservation;
import com.cinema.repository.ReservationRepo;
import com.cinema.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
@SpringBootTest
public class ReservationServiceUnitTest {
    @Mock
    private ReservationRepo reservationRepo;
    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        reservationRepo = Mockito.mock(ReservationRepo.class);
        reservationService = new ReservationService(reservationRepo);
    }

    @Test
    void addTest() {
        Reservation reservation = new Reservation();
        reservation.setName("ZażółćGęśląJaźń");
        reservation.setSurname("Zażółć-GęśląJaźń");
        reservation.setValue(new BigDecimal("12.5"));

        when(reservationRepo.save(reservation)).thenReturn(reservation);
        Reservation returnedReservation = this.reservationService.addReservation(reservation);

        assertEquals(reservation, returnedReservation);
        verify(this.reservationRepo).save(reservation);
    }
    
    @Test
    void findAllTest() {
        Reservation reservation = new Reservation();
        reservation.setName("ZażółćGęśląJaźń");
        reservation.setSurname("Zażółć-GęśląJaźń");
        reservation.setValue(new BigDecimal("12.5"));

        when(reservationRepo.findAll()).thenReturn(List.of(reservation));
        List<Reservation> reservations = this.reservationService.findAllReservations();

        assertEquals(List.of(reservation), reservations);
        verify(this.reservationRepo).findAll();
    }

    @Test
    void findByIdTest() {
        Reservation reservation = new Reservation();
        reservation.setId(2L);
        reservation.setName("ZażółćGęśląJaźń");
        reservation.setSurname("Zażółć-GęśląJaźń");
        reservation.setValue(new BigDecimal("12.5"));

        when(reservationRepo.findReservationById(anyLong())).thenReturn(Optional.of(reservation));
        Reservation returnedReservation = this.reservationService.findReservationById(2L);

        assertEquals(returnedReservation, reservation);
        verify(this.reservationRepo).findReservationById(2L);
    }

    @Test
    void updateTest() {
        Reservation reservation = new Reservation();
        reservation.setName("ZażółćGęśląJaźń");
        reservation.setSurname("Zażółć-GęśląJaźń");
        reservation.setValue(new BigDecimal("12.5"));

        when(reservationRepo.save(reservation)).thenReturn(reservation);
        Reservation returnedReservation = this.reservationService.addReservation(reservation);
        returnedReservation.setName("Aaa");
        Reservation returnedReservation2 = this.reservationService.updateReservation(returnedReservation);
        assertEquals(returnedReservation, returnedReservation2);
        verify(this.reservationRepo, times(2)).save(reservation);
    }

    @Test
    void findAllUnconfirmed() {
        Reservation reservation = new Reservation();
        reservation.setName("ZażółćGęśląJaźń");
        reservation.setSurname("Zażółć-GęśląJaźń");
        reservation.setValue(new BigDecimal("12.5"));
        reservation.setConfirmed(false);

        when(reservationRepo.findReservationByConfirmed(false)).thenReturn(Optional.of(List.of(reservation)));
        when(reservationRepo.findReservationByConfirmed(true)).thenThrow(new ReservationNotFoundException("No unconfirmed reservations found for deletion"));

        List<Reservation> reservations = this.reservationService.findAllUnconfirmedReservations(false);

        assertEquals(List.of(reservation), reservations);
        assertThrows(ReservationNotFoundException.class , () -> {this.reservationService.findAllUnconfirmedReservations(true);});

        verify(this.reservationRepo).findReservationByConfirmed(false);
        verify(this.reservationRepo).findReservationByConfirmed(true);
    }
}
