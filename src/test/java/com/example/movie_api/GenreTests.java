package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Genre;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GenreTests {

	@Autowired
	private TestRestTemplate restTemplate;

    @Test
	void getGenreList() {
		ResponseEntity<Genre[]> response = restTemplate.getForEntity("/genres", Genre[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(4);
	}

	@Test
	@DirtiesContext
	void createGenre() {
		Genre genre = new Genre("Horror");
		URI new_genre_location = restTemplate.postForLocation("/genres", genre, Void.class);
		assertNotNull(new_genre_location);
		ResponseEntity<Genre> response = restTemplate.getForEntity(new_genre_location, Genre.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("Horror");
	}

	@Test
	@DirtiesContext
	void deleteGenre() {
		ResponseEntity<Genre> response = restTemplate.getForEntity("/genres/1", Genre.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/genres/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		response = restTemplate.getForEntity("/genres/1", Genre.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void updateGenre() {
		Genre genre = new Genre("Documentary");
		restTemplate.put("/genres/1", genre);
		Genre updatedGenre = restTemplate.getForObject("/genres/1", Genre.class);
		assertThat(updatedGenre.getName()).isEqualTo("Documentary");
	}

	@Test
	void updatedGenreNotFound() {
		Genre genre = new Genre("Documentary");
		restTemplate.put("/genres/10", genre);
		ResponseEntity<Genre> response = restTemplate.getForEntity("/genres/10", Genre.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}
}
