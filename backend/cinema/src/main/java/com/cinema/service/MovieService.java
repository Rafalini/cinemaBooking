package com.cinema.service;

import com.cinema.exceptions.MovieNotFoundException;
import com.cinema.model.Movie;
import com.cinema.repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepo movieRepo;

    @Autowired
    public MovieService(MovieRepo movieRepo){
        this.movieRepo = movieRepo;
    }

    public Movie addMovie(Movie movie){
        return movieRepo.save(movie);
    }

    public List<Movie> findAllMovies(){
        return movieRepo.findAll();
    }

    public Movie findMovieById(Long id){
        return movieRepo.findMovieById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie "+id+" was not found"));
    }
}
