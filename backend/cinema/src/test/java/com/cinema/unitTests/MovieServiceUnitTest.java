package com.cinema.unitTests;

import com.cinema.exceptions.MovieNotFoundException;
import com.cinema.model.Movie;
import com.cinema.repository.MovieRepo;
import com.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MovieServiceUnitTest {
    @Mock
    private MovieRepo movieRepo;
    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setup() {
        movieRepo = Mockito.mock(MovieRepo.class);
        movieService = new MovieService(movieRepo);
    }

    @Test
    void addTest() {
        Movie movie = new Movie();
        movie.setTitle("Avatar");
        movie.setShortTitle("A");

        when(movieRepo.save(movie)).thenReturn(movie);
        Movie returnedMovie = this.movieService.addMovie(movie);

        assertEquals(movie, returnedMovie);
        verify(this.movieRepo).save(movie);
    }

    @Test
    void findAllTest() {
        Movie movie = new Movie();
        movie.setTitle("Avatar");
        movie.setShortTitle("A");

        when(movieRepo.findAll()).thenReturn(List.of(movie));
        List<Movie> movies = this.movieService.findAllMovies();

        assertEquals(List.of(movie), movies);
        verify(this.movieRepo).findAll();
    }

    @Test
    void findByIdTest() {
        Movie movie = new Movie();
        movie.setId(2L);
        movie.setTitle("Avatar");
        movie.setShortTitle("A");

        when(movieRepo.findMovieById(2L)).thenReturn(Optional.of(movie));
        when(movieRepo.findMovieById(404L)).thenThrow(new MovieNotFoundException("Message"));

        Movie returnedMovie = this.movieService.findMovieById(2L);
        assertThrows(MovieNotFoundException.class , () -> {this.movieService.findMovieById(404L);});

        assertEquals(returnedMovie, movie);
        verify(this.movieRepo).findMovieById(2L);
        verify(this.movieRepo).findMovieById(404L);
    }
}
