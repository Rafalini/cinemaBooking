package com.cinema.service;

import com.cinema.exceptions.MovieNotFoundException;
import com.cinema.exceptions.RoomNotFoundException;
import com.cinema.exceptions.ScreeningTimeNotFoundException;
import com.cinema.model.ScreeningTime;
import com.cinema.model.TemporalScreeningTime;
import com.cinema.repository.ScreeningRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService {

    private final ScreeningRepo screeningRepo;

    private final RoomService roomService;
    private final MovieService movieService;
    public ScreeningService(ScreeningRepo screeningRepo, RoomService roomService, MovieService movieService) {

        this.screeningRepo = screeningRepo;
        this.roomService = roomService;
        this.movieService = movieService;
    }

    public ScreeningTime addScreening(TemporalScreeningTime screening){
        ScreeningTime newScreening = new ScreeningTime();
        try {
            newScreening.setRoom(roomService.findRoomById(screening.roomId()));
            newScreening.setMovie(movieService.findMovieById(screening.movieId()));
        } catch (MovieNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Movie ID Not Found", ex);
        } catch (RoomNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Room ID Not Found", ex);
        }
        newScreening.setScreeningTime(screening.screeningTime());
        return screeningRepo.save(newScreening);
    }

    public List<ScreeningTime> findAllScreenings(){
        return screeningRepo.findAll();
    }

    public ScreeningTime findScreeningTimeById(Long id){
        return screeningRepo.findScreeningTimeById(id)
                .orElseThrow(() -> new ScreeningTimeNotFoundException("Screening "+id+" was not found"));
    }

    public List<ScreeningTime> findScreeningTimeByMovieId(Long id){
        return screeningRepo.findScreeningTimeByMovieId(id)
                .orElseThrow(() -> new ScreeningTimeNotFoundException("Screenings for movie "+id+" were not found"));
    }

    public List<ScreeningTime> findScreeningTimeByDate(LocalDate date){
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return screeningRepo.findByScreeningTimeBetweenOrderByMovie_Title(start,end)
                .orElseThrow(() -> new ScreeningTimeNotFoundException("Screenings for date "+date+" were not found"));
    }
}
