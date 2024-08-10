package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Cast;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Series;
import com.example.movie_api.utils.CustomPageImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CastTests {

	@Autowired
	private TestRestTemplate restTemplate;

    @Test
	@DirtiesContext
	void createCast() {
		Cast cast = new Cast("John Wick");
		URI new_cast_location = restTemplate.postForLocation("/casts", cast, Void.class);
		assertNotNull(new_cast_location);
		ResponseEntity<Cast> response = restTemplate.getForEntity(new_cast_location, Cast.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("John Wick");
	}

	@Test
	@DirtiesContext
	void deleteCast() {
		ResponseEntity<Cast> response = restTemplate.getForEntity("/casts/1", Cast.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/casts/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		response = restTemplate.getForEntity("/casts/1", Cast.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Movie[] movies = restTemplate.exchange(
			"/movies?page=1&size=10", HttpMethod.GET, null,
			new ParameterizedTypeReference<CustomPageImpl<Movie>>() {}
		).getBody().getContent().toArray(new Movie[0]);
		Series[] series = restTemplate.getForObject("/series", Series[].class);
		assertThat(movies.length).isEqualTo(4);
		assertThat(series.length).isEqualTo(3);
	}

	@Test
	void getCastList() {
		ResponseEntity<Cast[]> response = restTemplate.getForEntity("/casts", Cast[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(6);
	}

	@Test
	@DirtiesContext
	void updateCast() {
		Cast cast = new Cast("Emma Watson");
		restTemplate.put("/casts/1", cast);
		Cast updatedCast = restTemplate.getForObject("/casts/1", Cast.class);
		assertThat(updatedCast.getName()).isEqualTo("Emma Watson");
	}

	@Test
	void updatedCastNotFound() {
		Cast cast = new Cast("Lara Croft");
		restTemplate.put("/casts/10", cast);
		ResponseEntity<Cast> response = restTemplate.getForEntity("/casts/10", Cast.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}
}
