package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.Set;

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
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Production;
import com.example.movie_api.utils.CustomPageImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieTests {

	@Autowired
	private TestRestTemplate restTemplate;

    @Test
	void getMovieList() {
		ResponseEntity<CustomPageImpl<Movie>> response = restTemplate.exchange(
			"/movies?page=1&size=2&sort=imdbRating,desc", HttpMethod.GET,
			null, new ParameterizedTypeReference<CustomPageImpl<Movie>>(){}
		);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent().size()).isEqualTo(2);
		assertThat(response.getBody().getTotalPages()).isEqualTo(2);
		assertThat(response.getBody().getContent().get(0).getImdb_rating()).isEqualTo(9.3);
	}

	@Test
	@DirtiesContext
	void createMovie() {
		Movie movie = new Movie("The Matrix", "When a beautiful but troubled young woman comes to him with a dead body, Detective Fox Mulder is sent to investigate a bizarre crime.", 1999, 136, "USA", 8.7, "matrix.jpg");
		URI new_movie_location = restTemplate.postForLocation("/movies", movie, Void.class);
		assertNotNull(new_movie_location);
		ResponseEntity<Movie> response = restTemplate.getForEntity(new_movie_location, Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("The Matrix");
		assertThat(response.getBody().getDuration()).isEqualTo(136);
		assertThat(response.getBody().getRelease_year()).isEqualTo(1999);
		assertThat(response.getBody().getImdb_rating()).isEqualTo(8.7);
	}

	@Test
	@DirtiesContext
	void createMovieWithGenre() {
		Genre[] genres = restTemplate.getForObject("/genres", Genre[].class);
		Movie movie = new Movie("The Matrix", "When a beautiful but troubled young woman comes to him with a dead body, Detective Fox Mulder is sent to investigate a bizarre crime.", 1999, 136, "USA", 8.7, "matrix.jpg");
		movie.addGenre(genres[0]);
		movie.addGenre(genres[1]);
		URI new_movie_location = restTemplate.postForLocation("/movies", movie, Void.class);
		assertNotNull(new_movie_location);
		ResponseEntity<Movie> response = restTemplate.getForEntity(new_movie_location, Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getGenres().size()).isEqualTo(2);
		assertThat(response.getBody().getGenres().contains(genres[0])).isTrue();
		assertThat(response.getBody().getGenres().contains(genres[1])).isTrue();
		ResponseEntity<CustomPageImpl<Movie>> moviesListResponse = restTemplate.exchange(
			"/movies?page=1&size=1&sort=id,desc&allGenre=true&genre=" + genres[0].getName() + "," + genres[1].getName(),
			HttpMethod.GET, null,
			new ParameterizedTypeReference<CustomPageImpl<Movie>>(){}
		);
		Movie createdMovie = moviesListResponse.getBody().getContent().get(0);
		assertThat(createdMovie).isEqualTo(response.getBody());
	}

	@Test
	@DirtiesContext
	void createMovieWithCast() {
		Cast[] casts = restTemplate.getForObject("/casts", Cast[].class);
		Movie movie = new Movie("The Matrix", "When a beautiful but troubled young woman comes to him with a dead body, Detective Fox Mulder is sent to investigate a bizarre crime.", 1999, 136, "USA", 8.7, "matrix.jpg");
		movie.addCast(casts[0]);
		movie.addCast(casts[1]);
		URI new_movie_location = restTemplate.postForLocation("/movies", movie, Void.class);
		assertNotNull(new_movie_location);
		ResponseEntity<Movie> response = restTemplate.getForEntity(new_movie_location, Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getCasts().size()).isEqualTo(2);
		assertThat(response.getBody().getCasts().contains(casts[0])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[1])).isTrue();
		Cast c1 = restTemplate.getForObject("/casts/1", Cast.class);
		Cast c2 = restTemplate.getForObject("/casts/2", Cast.class);
		assertThat(c1.getMovies().contains(response.getBody())).isTrue();
		assertThat(c2.getMovies().contains(response.getBody())).isTrue();
	}

	@Test
	@DirtiesContext
	void createMovieWithProduction() {
		Production[] productions = restTemplate.getForObject("/productions", Production[].class);
		Movie movie = new Movie("The Matrix", "When a beautiful but troubled young woman comes to him with a dead body, Detective Fox Mulder is sent to investigate a bizarre crime.", 1999, 136, "USA", 8.7, "matrix.jpg");
		movie.addProduction(productions[0]);
		movie.addProduction(productions[1]);
		URI new_movie_location = restTemplate.postForLocation("/movies", movie, Void.class);
		assertNotNull(new_movie_location);
		ResponseEntity<Movie> response = restTemplate.getForEntity(new_movie_location, Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getProductions().size()).isEqualTo(2);
		assertThat(response.getBody().getProductions().contains(productions[0])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[1])).isTrue();
		Production p1 = restTemplate.getForObject("/productions/1", Production.class);
		Production p2 = restTemplate.getForObject("/productions/2", Production.class);
		assertThat(p1.getMovies().contains(response.getBody())).isTrue();
		assertThat(p2.getMovies().contains(response.getBody())).isTrue();
	}

	@Test
	@DirtiesContext
	void updateMovie() {
		Movie movie = new Movie("The Matrix", "When a beautiful but troubled young woman comes to him with a dead body, Detective Fox Mulder is sent to investigate a bizarre crime.", 1999, 136, "USA", 8.7, "matrix.jpg");
		restTemplate.put("/movies/1", movie);
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/1", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("The Matrix");
		assertThat(response.getBody().getDuration()).isEqualTo(136);
		assertThat(response.getBody().getRelease_year()).isEqualTo(1999);
		assertThat(response.getBody().getImdb_rating()).isEqualTo(8.7);
	}

	@Test
	@DirtiesContext
	void updateMovieGenre() {
		Genre[] genres = restTemplate.getForObject("/genres", Genre[].class);
		Movie movie = restTemplate.getForObject("/movies/1", Movie.class);
		movie.addGenre(genres[0]);
		movie.addGenre(genres[1]);
		restTemplate.put("/movies/1", movie);
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/1", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getGenres().size()).isEqualTo(2);
		assertThat(response.getBody().getGenres().contains(genres[0])).isTrue();
		assertThat(response.getBody().getGenres().contains(genres[1])).isTrue();
		Genre g1 = restTemplate.getForObject("/genres/1", Genre.class);
		Genre g2 = restTemplate.getForObject("/genres/2", Genre.class);
		movie = response.getBody();
		movie.getGenres().remove(g1);
		restTemplate.put("/movies/1", movie);
		movie = restTemplate.getForObject("/movies/1", Movie.class);
		assertThat(movie.getGenres().contains(g1)).isFalse();
		assertThat(movie.getGenres().contains(g2)).isTrue();
	}

	@Test
	@DirtiesContext
	void updateMovieCast() {
		Cast[] casts = restTemplate.getForObject("/casts", Cast[].class);
		Movie movie = restTemplate.getForObject("/movies/1", Movie.class);
		movie.addCast(casts[0]);
		movie.addCast(casts[1]);
		restTemplate.put("/movies/1", movie);
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/1", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getCasts().size()).isEqualTo(2);
		assertThat(response.getBody().getCasts().contains(casts[0])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[1])).isTrue();
		Cast c1 = restTemplate.getForObject("/casts/1", Cast.class);
		Cast c2 = restTemplate.getForObject("/casts/2", Cast.class);
		assertThat(c1.getMovies().contains(response.getBody())).isTrue();
		assertThat(c2.getMovies().contains(response.getBody())).isTrue();
		movie = response.getBody();
		movie.getCasts().remove(c1);
		restTemplate.put("/movies/1", movie);
		movie = restTemplate.getForObject("/movies/1", Movie.class);
		assertThat(movie.getCasts().contains(c1)).isFalse();
		assertThat(movie.getCasts().contains(c2)).isTrue();
		c1 = restTemplate.getForObject("/casts/1", Cast.class);
		c2 = restTemplate.getForObject("/casts/2", Cast.class);
		assertThat(c1.getMovies().contains(movie)).isFalse();
		assertThat(c2.getMovies().contains(movie)).isTrue();
	}

	@Test
	@DirtiesContext
	void updateMovieProduction() {
		Production[] productions = restTemplate.getForObject("/productions", Production[].class);
		Movie movie = restTemplate.getForObject("/movies/1", Movie.class);
		movie.addProduction(productions[0]);
		movie.addProduction(productions[1]);
		restTemplate.put("/movies/1", movie);
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/1", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getProductions().size()).isEqualTo(2);
		assertThat(response.getBody().getProductions().contains(productions[0])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[1])).isTrue();
		Production p1 = restTemplate.getForObject("/productions/1", Production.class);
		Production p2 = restTemplate.getForObject("/productions/2", Production.class);
		assertThat(p1.getMovies().contains(response.getBody())).isTrue();
		assertThat(p2.getMovies().contains(response.getBody())).isTrue();
		movie = response.getBody();
		movie.getProductions().remove(p1);
		restTemplate.put("/movies/1", movie);
		movie = restTemplate.getForObject("/movies/1", Movie.class);
		assertThat(movie.getProductions().contains(p1)).isFalse();
		assertThat(movie.getProductions().contains(p2)).isTrue();
		p1 = restTemplate.getForObject("/productions/1", Production.class);
		p2 = restTemplate.getForObject("/productions/2", Production.class);
		assertThat(p1.getMovies().contains(movie)).isFalse();
		assertThat(p2.getMovies().contains(movie)).isTrue();
	}

	@Test
	@DirtiesContext
	void deleteMovie() {
		restTemplate.delete("/movies/1");
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/1", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void deleteMovieWithRelationships() {
		Movie movie = restTemplate.getForObject("/movies/3", Movie.class);
		Set<Genre> genres = movie.getGenres();
		Set<Cast> casts = movie.getCasts();
		Set<Production> productions = movie.getProductions();
		restTemplate.delete("/movies/3");
		ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/3", Movie.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		for (Genre genre : genres) {
			ResponseEntity<Genre> genre_response = restTemplate.getForEntity("/genres/" + genre.getId(), Genre.class);
			assertThat(genre_response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(genre_response.getBody().getMovies().contains(movie)).isFalse();
		}
		for (Cast cast : casts) {
			ResponseEntity<Cast> cast_response = restTemplate.getForEntity("/casts/" + cast.getId(), Cast.class);
			assertThat(cast_response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(cast_response.getBody().getMovies().contains(movie)).isFalse();
		}
		for (Production production : productions) {
			ResponseEntity<Production> production_response = restTemplate.getForEntity("/productions/" + production.getId(), Production.class);
			assertThat(production_response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(production_response.getBody().getMovies().contains(movie)).isFalse();
		}
	}
}
