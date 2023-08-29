package com.cinema.repository;

import com.cinema.model.ScreeningTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepo extends JpaRepository<ScreeningTime, Long> {
    Optional<ScreeningTime> findScreeningTimeById(Long id);

    Optional<List<ScreeningTime>> findScreeningTimeByMovieId(Long id);

    Optional<List<ScreeningTime>> findByScreeningTimeBetweenOrderByMovie_Title(LocalDateTime start, LocalDateTime end);
}
