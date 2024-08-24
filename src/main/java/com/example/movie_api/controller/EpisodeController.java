package com.example.movie_api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Review;
import com.example.movie_api.service.EpisodeService;

@RestController
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @GetMapping("/episodes")
    public ResponseEntity<Iterable<Episode>> getEpisodeList() {
        return ResponseEntity.ok(episodeService.getEpisodes());
    }

    @GetMapping("/episodes/{episodeId}")
    public ResponseEntity<Episode> getEpisode(@PathVariable Long episodeId) {
        Episode episode;
        try {
            episode = episodeService.getEpisode(episodeId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(episode);
    }

    @PostMapping("/episodes")
    public ResponseEntity<Void> createEpisode(@RequestParam("season_id") Long seasonId, @RequestBody Episode episode) {
        Episode  createdEpisode;
        try {
            createdEpisode = episodeService.createEpisode(seasonId, episode);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        URI new_episode_location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .path("/{resourceId}")
            .buildAndExpand(createdEpisode.getId())
            .toUri();
        return ResponseEntity.created(new_episode_location).build();
    }

    @PostMapping("/episodes/{episodeId}/reviews")
    public ResponseEntity<Void> createReview(@PathVariable Long episodeId, @RequestBody Review review, Principal principal) {
        Review createdReview = episodeService.createReview(episodeId, review, principal.getName());
        URI new_review_location = UriComponentsBuilder
            .fromPath("/reviews/{resourceId}")
            .buildAndExpand(createdReview.getId())
            .toUri();
        return ResponseEntity.created(new_review_location).build();
    }

    @PutMapping("/episodes/{episodeId}")
    public ResponseEntity<Void> updateEpisode(@PathVariable Long episodeId, @RequestBody Episode episode) {
        try {
            episodeService.updateEpisode(episodeId, episode);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/episodes/{episodeId}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long episodeId) {
        episodeService.deleteEpisode(episodeId);
        return ResponseEntity.noContent().build();
    }
}
