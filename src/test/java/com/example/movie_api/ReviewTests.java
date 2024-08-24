package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Review;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;
import com.example.movie_api.utils.CustomPageImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReviewTests extends MovieApiApplicationTests {
    @Test
    public void getReviewList() {
        ResponseEntity<CustomPageImpl<Review>> response = authRT()
            .exchange("/reviews?page=1&size=10", HttpMethod.GET, null,
            new ParameterizedTypeReference<CustomPageImpl<Review>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isEqualTo(6);
    }

    @Test
    @DirtiesContext
    public void createMovieReview() {
        Review review = new Review(8, "Great movie!");
        URI new_review_location = authRT().postForLocation("/movies/1/reviews", review, Void.class);
        ResponseEntity<Review> response = authRT().getForEntity(new_review_location, Review.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRating()).isEqualTo(8);
        assertThat(response.getBody().getComment()).isEqualTo("Great movie!");
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("admin");
        Movie movie = authRT().getForObject("/movies/1", Movie.class);
        assertThat(String.format("%.1f", movie.getUserRating())).isEqualTo("8.3");
        assertThat(movie.getReviewCount()).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    public void createSeriesReview() {
        Review review = new Review(10, "Great series!");
        URI new_review_location = authRT().postForLocation("/series/2/reviews", review, Void.class);
        ResponseEntity<Review> response = authRT().getForEntity(new_review_location, Review.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRating()).isEqualTo(10);
        assertThat(response.getBody().getComment()).isEqualTo("Great series!");
        Series series = authRT().getForObject("/series/2", Series.class);
        assertThat(String.format("%.1f", series.getUserRating())).isEqualTo("10.0");
        assertThat(series.getReviewCount()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void createSeasonReview() {
        Review review = new Review(9, "Great season!");
        URI new_review_location = authRT().postForLocation("/seasons/1/reviews", review, Void.class);
        ResponseEntity<Review> response = authRT().getForEntity(new_review_location, Review.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRating()).isEqualTo(9);
        assertThat(response.getBody().getComment()).isEqualTo("Great season!");
        Season season = authRT().getForObject("/seasons/1", Season.class);
        assertThat(String.format("%.1f", season.getUserRating())).isEqualTo("9.3");
        assertThat(season.getReviewCount()).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    public void createEpisodeReview() {
        Review review = new Review(5, "Great episode!");
        URI new_review_location = authRT().postForLocation("/episodes/1/reviews", review, Void.class);
        ResponseEntity<Review> response = authRT().getForEntity(new_review_location, Review.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRating()).isEqualTo(5);
        assertThat(response.getBody().getComment()).isEqualTo("Great episode!");
        Episode episode = authRT().getForObject("/episodes/1", Episode.class);
        assertThat(String.format("%.1f", episode.getUserRating())).isEqualTo("8.0");
        assertThat(episode.getReviewCount()).isEqualTo(3);
    }
}
