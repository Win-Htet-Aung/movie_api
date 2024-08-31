package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Season;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EpisodeTests extends MovieApiApplicationTests {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void getEpisodeList() {
        ResponseEntity<Episode[]> response = authRT("user").getForEntity("/episodes", Episode[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void createEpisode() throws ParseException {
        Episode episode = new Episode(1, "Episode 1 of House of Cards Season 1.", df.parse("2013-11-07"), 8.8);
        URI new_episode_location = authRT("admin").postForLocation("/episodes?season_id=1", episode);
        ResponseEntity<Episode> response = authRT("user").getForEntity(new_episode_location, Episode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAir_date()).isEqualTo(episode.getAir_date());
        assertThat(response.getBody().getEpisode_number()).isEqualTo(episode.getEpisode_number());
        assertThat(response.getBody().getTitle()).isEqualTo(episode.getTitle());
        assertThat(response.getBody().getImdb_rating()).isEqualTo(episode.getImdb_rating());
        assertThat(response.getBody().getSeason().getId()).isEqualTo(1);
        Season season = authRT("user").getForObject("/seasons/1", Season.class);
        assertThat(season.getEpisodes().size()).isEqualTo(1);
        assertThat(season.getEpisodes().contains(response.getBody())).isTrue();
    }

    @Test
    @DirtiesContext
    public void createEpisodeNoSeason() throws ParseException {
        Episode episode = new Episode(1, "Episode 1 of House of Cards Season 1.", df.parse("2013-11-07"), 8.8);
        ResponseEntity<Void> response = authRT("admin").postForEntity("/episodes", episode, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        response = authRT("admin").postForEntity("/episodes?season_id=10", episode, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void updateEpisode() throws ParseException {
        Episode episode = new Episode(1, "Episode 1 Season 2.", df.parse("2013-11-14"), 8.0);
        authRT("admin").put("/episodes/1", episode);
        ResponseEntity<Episode> response = authRT("user").getForEntity("/episodes/1", Episode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAir_date()).isEqualTo(episode.getAir_date());
        assertThat(response.getBody().getEpisode_number()).isEqualTo(episode.getEpisode_number());
        assertThat(response.getBody().getTitle()).isEqualTo(episode.getTitle());
        assertThat(response.getBody().getImdb_rating()).isEqualTo(episode.getImdb_rating());
        episode.setEpisode_number(2);
        ResponseEntity<Void> err_response = authRT("admin").exchange("/episodes/1", HttpMethod.PUT, new HttpEntity<>(episode), Void.class);
        assertThat(err_response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DirtiesContext
    public void updateEpisodeWithRlationships() throws ParseException {
        Season season = authRT("user").getForObject("/seasons/1", Season.class);
        Episode episode = new Episode(1, "Episode 1 Season 1.", df.parse("2013-11-14"), 8.0);
        episode.setSeason(season);
        authRT("admin").put("/episodes/2", episode);
        season = authRT("user").getForObject("/seasons/1", Season.class);
        episode = authRT("user").getForObject("/episodes/2", Episode.class);
        assertThat(season.getEpisodes().size()).isEqualTo(1);
        assertThat(season.getEpisodes().contains(episode)).isTrue();
        assertThat(episode.getSeason().getId()).isEqualTo(1);
        season = authRT("user").getForObject("/seasons/2", Season.class);
        assertThat(season.getEpisodes().size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void deleteEpisode() {
        authRT("user").delete("/episodes/1");
        ResponseEntity<Episode> response = authRT("user").getForEntity("/episodes/1", Episode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Season season = authRT("user").getForObject("/seasons/2", Season.class);
        assertThat(season.getEpisodes().size()).isEqualTo(1);
    }
}
