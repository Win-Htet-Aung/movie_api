package com.example.movie_api.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.model.Movie;
import com.example.movie_api.service.MovieService;

@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<Iterable<Movie>> getMovieList() {
        return ResponseEntity.ok(movieService.getMovieList());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long movieId) {
        Movie movie;
        try {
            movie = movieService.getMovie(movieId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    // @PostMapping("/movies")
    // public ResponseEntity<Void> createMovie(@RequestBody Movie newMovie) {
}
