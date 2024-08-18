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

import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Review;
import com.example.movie_api.utils.CustomPageImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReviewTests extends MovieApiApplicationTests {
    @Test
    public void getReviewList() {
        ResponseEntity<CustomPageImpl<Review>> response = authRT()
            .exchange("/reviews?page=1&size=10", HttpMethod.GET, null,
            new ParameterizedTypeReference<CustomPageImpl<Review>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void createReview() {
        Review review = new Review(8.0, "Great movie!");
        URI new_review_location = authRT().postForLocation("/movies/2/reviews", review, Void.class);
        ResponseEntity<Review> response = authRT().getForEntity(new_review_location, Review.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRating()).isEqualTo(8.0);
        assertThat(response.getBody().getComment()).isEqualTo("Great movie!");
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("admin");
        Movie movie = authRT().getForObject("/movies/2", Movie.class);
        assertThat(response.getBody().getMovies().contains(movie)).isTrue();
        assertThat(movie.getUserRating()).isEqualTo(7.5);
    }
}
