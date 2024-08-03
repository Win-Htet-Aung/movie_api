package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
