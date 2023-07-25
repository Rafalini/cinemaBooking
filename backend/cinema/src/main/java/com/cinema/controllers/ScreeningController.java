package com.cinema.controllers;

import com.cinema.exceptions.MovieNotFoundException;
import com.cinema.exceptions.RoomNotFoundException;
import com.cinema.model.ScreeningTime;
import com.cinema.model.TemporalScreeningTime;
import com.cinema.service.MovieService;
import com.cinema.service.RoomService;
import com.cinema.service.ScreeningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;

@RestController
@RequestMapping("/screening")
public class ScreeningController {
    private final ScreeningService screeningService;
    private final RoomService roomService;
    private final MovieService movieService;

    public ScreeningController(ScreeningService screeningService, RoomService roomService,  MovieService movieService) {
        this.screeningService = screeningService;
        this.roomService = roomService;
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ScreeningTime>> getScreeningsByDate(@RequestParam LocalDate date){
        List<ScreeningTime> screenings = null;
        screenings = screeningService.findScreeningTimeByDate(date);
        screenings.sort(Comparator.comparing(x -> x.getMovie().getTitle()));
        return new ResponseEntity<>(screenings, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ScreeningTime> addScreeningTime(@RequestBody TemporalScreeningTime screening){
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
        ScreeningTime readyScreening = screeningService.addScreening(newScreening);
        return new ResponseEntity<>(readyScreening, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreeningTime> getScreeningTimeById(@PathVariable("id") Long id){
        ScreeningTime screeningTime = screeningService.findScreeningTimeById(id);
        return new ResponseEntity<>(screeningTime, HttpStatus.OK);
    }
}
