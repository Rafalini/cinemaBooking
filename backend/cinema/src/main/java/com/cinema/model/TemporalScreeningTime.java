package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
//Record screening data model sent from frontend to create Reservation (id's instead references)

public record TemporalScreeningTime(Long movieId, Long roomId,
                                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime screeningTime) {
    public TemporalScreeningTime(Long movieId, Long roomId, LocalDateTime screeningTime) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.screeningTime = screeningTime;
    }
}
