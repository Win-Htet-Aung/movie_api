package com.example.movie_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.movie_api.dto.MovieSeries;
import com.example.movie_api.utils.CustomPageImpl;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getSearchPage() {
        ResponseEntity<CustomPageImpl<MovieSeries>> response = restTemplate.exchange(
            "/search?query=&page=1&size=10", HttpMethod.GET, null,
            new ParameterizedTypeReference<CustomPageImpl<MovieSeries>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isEqualTo(7);
    }
}
