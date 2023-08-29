package com.cinema.controllers;

import com.cinema.model.Movie;
import com.cinema.service.MovieService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies(){
        return movieService.findAllMovies();
    }

    @PostMapping("/add")
    public Movie addMovie(@RequestBody Movie movie){
        Movie newMovie = movieService.addMovie(movie);
        return movieService.addMovie(movie);
    }
}
