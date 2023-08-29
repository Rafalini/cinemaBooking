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

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/all")
    public List<ScreeningTime> getScreeningsByDate(@RequestParam LocalDate date){
        return screeningService.findScreeningTimeByDate(date);
    }

    @PostMapping("/add")
    public ScreeningTime addScreeningTime(@RequestBody TemporalScreeningTime screening){
        return screeningService.addScreening(screening);
    }

    @GetMapping("/{id}")
    public ScreeningTime getScreeningTimeById(@PathVariable("id") Long id){
        return screeningService.findScreeningTimeById(id);
    }
}
