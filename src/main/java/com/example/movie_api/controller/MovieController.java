package com.example.movie_api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Review;
import com.example.movie_api.service.MovieService;

@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<Page<Movie>> getMovieList(Pageable pageable) {
        Page<Movie> page = movieService.getMovieList(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/movies/{slug}")
    public ResponseEntity<Movie> getMovie(@PathVariable String slug) {
        Movie movie;
        try {
            movie = movieService.getMovie(slug);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    @PostMapping("/movies/{slug}/reviews")
    public ResponseEntity<Void> createReview(@PathVariable String slug, @RequestBody Review newReview, Principal principal) {
        Review createdReview = movieService.createReview(slug, newReview, principal.getName());
        URI new_review_location = UriComponentsBuilder
                .fromPath("/reviews/{resourceId}")
                .buildAndExpand(createdReview.getId())
                .toUri();
        return ResponseEntity.created(new_review_location).build();
    }

    @PostMapping("/movies")
    public ResponseEntity<Void> createMovie(@RequestBody Movie newMovie) {
        Movie createdMovie = movieService.createMovie(newMovie);
        URI new_movie_location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{resourceId}")
                .buildAndExpand(createdMovie.getSlug())
                .toUri();
        return ResponseEntity.created(new_movie_location).build();
    }

    @PutMapping("/movies/{slug}")
    public ResponseEntity<Void> updateMovie(@PathVariable String slug, @RequestBody Movie updatedMovie) {
        try {
            movieService.getMovie(slug);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        movieService.updateMovie(slug, updatedMovie);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}
