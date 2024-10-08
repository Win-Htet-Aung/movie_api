package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SeasonTests extends MovieApiApplicationTests {

    @Test
    void getSeasonList() {
        ResponseEntity<Season[]> response = authRT("user").getForEntity("/seasons", Season[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    void createSeason() {
        Season season = new Season(1, "Season 1", 2021, 8.5);
        URI new_season_location = authRT("admin").postForLocation("/seasons?series_id=1", season, Void.class);
        ResponseEntity<Season> response = authRT("user").getForEntity(new_season_location, Season.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSeason_number()).isEqualTo(season.getSeason_number());
        assertThat(response.getBody().getSummary()).isEqualTo(season.getSummary());
        assertThat(response.getBody().getRelease_year()).isEqualTo(season.getRelease_year());
        assertThat(response.getBody().getImdb_rating()).isEqualTo(season.getImdb_rating());
        assertThat(response.getBody().getSeries().getId()).isEqualTo(1);
        Series series = authRT("user").getForObject("/series/1", Series.class);
        assertThat(series.getSeasons().size()).isEqualTo(1);
        assertThat(series.getSeasons().contains(response.getBody())).isTrue();
    }

    @Test
    @DirtiesContext
    void createSeasonNoSeries() {
        Season season = new Season(1, "Season 1", 2021, 8.5);
        ResponseEntity<Void> response = authRT("admin").postForEntity("/seasons", season, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        response = authRT("admin").postForEntity("/seasons?series_id=10", season, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void updateSeason() {
        Season season = new Season(1, "Season 1", 2021, 8.5);
        authRT("admin").put("/seasons/1", season);
        ResponseEntity<Season> response = authRT("user").getForEntity("/seasons/1", Season.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSeason_number()).isEqualTo(season.getSeason_number());
        assertThat(response.getBody().getSummary()).isEqualTo(season.getSummary());
        assertThat(response.getBody().getRelease_year()).isEqualTo(season.getRelease_year());
        assertThat(response.getBody().getImdb_rating()).isEqualTo(season.getImdb_rating());
    }

    @Test
    @DirtiesContext
    void updateSeasonWithRlationships() {
        Series series = authRT("user").getForObject("/series/2", Series.class);
        Season season = new Season(1, "Season 1", 2021, 8.5);
        season.setSeries(series);
        authRT("admin").put("/seasons/2", season);
        series = authRT("user").getForObject("/series/2", Series.class);
        season = authRT("user").getForObject("/seasons/2", Season.class);
        assertThat(series.getSeasons().size()).isEqualTo(1);
        assertThat(series.getSeasons().contains(season)).isTrue();
        assertThat(season.getSeries().getId()).isEqualTo(2);
        series = authRT("user").getForObject("/series/3", Series.class);
        assertThat(series.getSeasons().size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void deleteSeason() {
        authRT("admin").delete("/seasons/2");
        ResponseEntity<Season> response = authRT("user").getForEntity("/seasons/2", Season.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Series series = authRT("user").getForObject("/series/3", Series.class);
        assertThat(series.getSeasons().size()).isEqualTo(1);
        Episode[] episodes = authRT("user").getForObject("/episodes", Episode[].class);
        assertThat(episodes.length).isEqualTo(0);
    }
}
