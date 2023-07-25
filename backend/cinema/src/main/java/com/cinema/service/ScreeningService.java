package com.cinema.service;

import com.cinema.exceptions.ScreeningTimeNotFoundException;
import com.cinema.model.ScreeningTime;
import com.cinema.repository.ScreeningRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService {

    private final ScreeningRepo screeningRepo;

    public ScreeningService(ScreeningRepo screeningRepo) {
        this.screeningRepo = screeningRepo;
    }

    public ScreeningTime addScreening(ScreeningTime screeningTime){
        return screeningRepo.save(screeningTime);
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
        return screeningRepo.findByScreeningTimeBetween(start,end)
                .orElseThrow(() -> new ScreeningTimeNotFoundException("Screenings for date "+date+" were not found"));
    }
}
