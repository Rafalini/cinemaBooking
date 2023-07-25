package com.cinema.unitTests;

import com.cinema.exceptions.ScreeningTimeNotFoundException;
import com.cinema.model.Movie;
import com.cinema.model.ScreeningTime;
import com.cinema.repository.ScreeningRepo;
import com.cinema.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ScreeningServiceUnitTest {
    @Mock
    private ScreeningRepo screeningRepo;
    @InjectMocks
    private ScreeningService screeningService;

    @BeforeEach
    void setup() {
        screeningRepo = Mockito.mock(ScreeningRepo.class);
        screeningService = new ScreeningService(screeningRepo);
    }

    @Test
    void addTest() {
        ScreeningTime screeningTime = new ScreeningTime();
        screeningTime.setScreeningTime(LocalDateTime.now());

        when(screeningRepo.save(screeningTime)).thenReturn(screeningTime);
        ScreeningTime returnedScreeningTime = this.screeningService.addScreening(screeningTime);

        assertEquals(screeningTime, returnedScreeningTime);
        verify(this.screeningRepo).save(screeningTime);
    }
    
    @Test
    void findAllTest() {
        ScreeningTime screeningTime = new ScreeningTime();
        screeningTime.setScreeningTime(LocalDateTime.now());

        when(screeningRepo.findAll()).thenReturn(List.of(screeningTime));
        List<ScreeningTime> screeningTimes = this.screeningService.findAllScreenings();

        assertEquals(List.of(screeningTime), screeningTimes);
        verify(this.screeningRepo).findAll();
    }

    @Test
    void findById() {
        ScreeningTime screeningTime = new ScreeningTime();
        screeningTime.setId(2L);
        screeningTime.setScreeningTime(LocalDateTime.now());

        when(screeningRepo.findScreeningTimeById(2L)).thenReturn(Optional.of(screeningTime));
        when(screeningRepo.findScreeningTimeById(404L)).thenThrow(new ScreeningTimeNotFoundException("Message"));
        ScreeningTime returnedScreening = this.screeningService.findScreeningTimeById(2L);

        assertEquals(screeningTime, returnedScreening);
        assertThrows(ScreeningTimeNotFoundException.class , () -> {this.screeningService.findScreeningTimeById(404L);});

        verify(this.screeningRepo).findScreeningTimeById(2L);
        verify(this.screeningRepo).findScreeningTimeById(404L);
    }
    @Test
    void findByMovieId() {
        ScreeningTime screeningTime = new ScreeningTime();
        Movie movie = new Movie();
        movie.setId(1L);
        screeningTime.setScreeningTime(LocalDateTime.now());
        screeningTime.setMovie(movie);

        when(screeningRepo.findScreeningTimeByMovieId(1L)).thenReturn(Optional.of(List.of(screeningTime)));
        when(screeningRepo.findScreeningTimeByMovieId(404L)).thenThrow(new ScreeningTimeNotFoundException("Message"));
        List<ScreeningTime> screeningTimes = this.screeningService.findScreeningTimeByMovieId(1L);

        assertEquals(List.of(screeningTime), screeningTimes);
        assertThrows(ScreeningTimeNotFoundException.class , () -> {this.screeningService.findScreeningTimeByMovieId(404L);});

        verify(this.screeningRepo).findScreeningTimeByMovieId(1L);
        verify(this.screeningRepo).findScreeningTimeByMovieId(404L);
    }

    @Test
    void findByDate() {
        ScreeningTime screeningTime = new ScreeningTime();
        LocalDate date = LocalDate.now();
        LocalDate pastDate = date.minusDays(1);
        LocalDateTime start = date.atStartOfDay();
        screeningTime.setScreeningTime(start.plusHours(1));

        when(screeningRepo.findByScreeningTimeBetween(start,start.plusDays(1))).thenReturn(Optional.of(List.of(screeningTime)));
        when(screeningRepo.findByScreeningTimeBetween(start.minusDays(1), start)).thenThrow(new ScreeningTimeNotFoundException("Message"));
        List<ScreeningTime> screeningTimes = this.screeningService.findScreeningTimeByDate(date);

        assertEquals(List.of(screeningTime), screeningTimes);
        assertThrows(ScreeningTimeNotFoundException.class , () -> {this.screeningService.findScreeningTimeByDate(pastDate);});

        verify(this.screeningRepo).findByScreeningTimeBetween(start,start.plusDays(1));
        verify(this.screeningRepo).findByScreeningTimeBetween(start.minusDays(1), start);
    }
}
