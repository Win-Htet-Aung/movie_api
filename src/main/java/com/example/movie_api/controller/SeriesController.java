package com.example.movie_api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.movie_api.model.Review;
import com.example.movie_api.model.Series;
import com.example.movie_api.service.SeriesService;

@RestController
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @GetMapping("/series")
    public ResponseEntity<Iterable<Series>> getSeriesList() {
        return ResponseEntity.ok(seriesService.getSeriesList());
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<Series> getSeries(@PathVariable Long seriesId) {
        Series series;
        try {
            series = seriesService.getSeries(seriesId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(series);
    }

    @PostMapping("/series")
    public ResponseEntity<Void> createSeries(@RequestBody Series newSeries) {
        Series createdSeries = seriesService.createSeries(newSeries);
        URI new_series_location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{resourceId}")
                .buildAndExpand(createdSeries.getId())
                .toUri();
        return ResponseEntity.created(new_series_location).build();
    }

    @PutMapping("/series/{seriesId}")
    public ResponseEntity<Void> updateSeries(@PathVariable Long seriesId, @RequestBody Series updatedSeries) {
        seriesService.updateSeries(seriesId, updatedSeries);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{seriesId}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long seriesId) {
        seriesService.deleteSeries(seriesId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/series/{seriesId}/reviews")
    public ResponseEntity<Void> createReview(@PathVariable Long seriesId, @RequestBody Review review, Principal principal) {
        Review createdReview = seriesService.createReview(seriesId, review, principal.getName());
        URI new_review_location = UriComponentsBuilder
            .fromPath("/reviews/{resourceId}")
            .buildAndExpand(createdReview.getId())
            .toUri();
        return ResponseEntity.created(new_review_location).build();
    }
}
