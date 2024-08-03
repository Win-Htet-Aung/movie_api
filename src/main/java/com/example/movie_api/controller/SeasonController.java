package com.example.movie_api.controller;

import java.net.URI;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.movie_api.model.Season;
import com.example.movie_api.service.SeasonService;

@RestController
public class SeasonController {
    @Autowired
    private SeasonService seasonService;

    @GetMapping("/seasons")
    public ResponseEntity<Iterable<Season>> getSeasonList() {
        return ResponseEntity.ok(seasonService.getSeasonList());
    }

    @GetMapping("/seasons/{seasonId}")
    public ResponseEntity<Season> getSeason(@PathVariable Long seasonId) {
        Season season;
        try {
            season = seasonService.getSeason(seasonId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(season);
    }

    @PostMapping("/seasons")
    public ResponseEntity<Void> createSeason(@RequestBody Season season, @RequestParam("series_id") Long seriesId) {
        Season newSeason;
        try {
            newSeason = seasonService.createSeason(seriesId, season);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        URI newSeasonLocation = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{resourceId}")
            .buildAndExpand(newSeason.getId())
            .toUri();
        return ResponseEntity.created(newSeasonLocation).build();
    }
}
