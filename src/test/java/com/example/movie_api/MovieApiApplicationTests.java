package com.example.movie_api;

import java.net.URI;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.movie_api.model.Cast;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Production;
import com.example.movie_api.model.Series;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieApiApplicationTests {

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

	@Test
	void getProductionList() {
		ResponseEntity<Production[]> response = restTemplate.getForEntity("/productions", Production[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(6);
	}

	@Test
	@DirtiesContext
	void createProduction() {
		Production production = new Production("Star Wars");
		URI new_production_location = restTemplate.postForLocation("/productions", production, Void.class);
		assertNotNull(new_production_location);
		ResponseEntity<Production> response = restTemplate.getForEntity(new_production_location, Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("Star Wars");
	}

	@Test
	@DirtiesContext
	void updateProduction() {
		Production production = new Production("Marvel Cinematic Universe");
		restTemplate.put("/productions/1", production);
		Production updatedProduction = restTemplate.getForObject("/productions/1", Production.class);
		assertThat(updatedProduction.getName()).isEqualTo("Marvel Cinematic Universe");
	}

	@Test
	void updatedProductionNotFound() {
		Production production = new Production("20th Century Fox");
		restTemplate.put("/productions/10", production);
		ResponseEntity<Production> response = restTemplate.getForEntity("/productions/10", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}

	@Test
	@DirtiesContext
	void deleteProduction() {
		ResponseEntity<Production> response = restTemplate.getForEntity("/productions/1", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/productions/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		response = restTemplate.getForEntity("/productions/1", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void getMovieList() {
		ResponseEntity<Movie[]> response = restTemplate.getForEntity("/movies", Movie[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(3);
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
		Genre g1 = restTemplate.getForObject("/genres/1", Genre.class);
		Genre g2 = restTemplate.getForObject("/genres/2", Genre.class);
		assertThat(g1.getMovies().contains(response.getBody())).isTrue();
		assertThat(g2.getMovies().contains(response.getBody())).isTrue();
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
		assertThat(g1.getMovies().contains(response.getBody())).isTrue();
		assertThat(g2.getMovies().contains(response.getBody())).isTrue();
		movie = response.getBody();
		movie.getGenres().remove(g1);
		restTemplate.put("/movies/1", movie);
		movie = restTemplate.getForObject("/movies/1", Movie.class);
		assertThat(movie.getGenres().contains(g1)).isFalse();
		assertThat(movie.getGenres().contains(g2)).isTrue();
		g1 = restTemplate.getForObject("/genres/1", Genre.class);
		g2 = restTemplate.getForObject("/genres/2", Genre.class);
		assertThat(g1.getMovies().contains(movie)).isFalse();
		assertThat(g2.getMovies().contains(movie)).isTrue();
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

	@Test
	void getSeriesList() {
		ResponseEntity<Series[]> response = restTemplate.getForEntity("/series", Series[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(3);
	}

	@Test
	@DirtiesContext
	void createSeries() {
		Series series = new Series("Breaking Bad", "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine.", 2008, 49, "USA", 9.5, "breaking_bad.jpg");
		URI new_series_location = restTemplate.postForLocation("/series", series, Void.class);
		ResponseEntity<Series> response = restTemplate.getForEntity(new_series_location, Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(series.getTitle());
		assertThat(response.getBody().getRelease_year()).isEqualTo(series.getRelease_year());
		assertThat(response.getBody().getCountry()).isEqualTo(series.getCountry());
		assertThat(response.getBody().getImdb_rating()).isEqualTo(series.getImdb_rating());
	}

	@Test
	@DirtiesContext
	void createSeriesWithRelationships() {
		Genre[] genres = restTemplate.getForObject("/genres", Genre[].class);
		Cast[] casts = restTemplate.getForObject("/casts", Cast[].class);
		Production[] productions = restTemplate.getForObject("/productions", Production[].class);
		Series series = new Series("The Wire", "An American news satire and cable drama series.", 2002, 45, "USA", 9.0, "the_wire.jpg");
		series.addGenre(genres[0]);
		series.addGenre(genres[1]);
		series.addCast(casts[0]);
		series.addCast(casts[1]);
		series.addProduction(productions[0]);
		series.addProduction(productions[1]);
		URI new_series_location = restTemplate.postForLocation("/series", series, Void.class);
		ResponseEntity<Series> response = restTemplate.getForEntity(new_series_location, Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getGenres().contains(genres[0])).isTrue();
		assertThat(response.getBody().getGenres().contains(genres[1])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[0])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[1])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[0])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[1])).isTrue();
		Series createdSeries = response.getBody();
		Genre g = restTemplate.getForObject("/genres/" + genres[0].getId(), Genre.class);
		Cast c = restTemplate.getForObject("/casts/" + casts[0].getId(), Cast.class);
		Production p = restTemplate.getForObject("/productions/" + productions[0].getId(), Production.class);
		assertThat(g.getSeries().contains(createdSeries)).isTrue();
		assertThat(c.getSeries().contains(createdSeries)).isTrue();
		assertThat(p.getSeries().contains(createdSeries)).isTrue();
	}

	@Test
	@DirtiesContext
	void updateSeries() {
		Series series = new Series("The Wire", "An American news satire and cable drama series.", 2002, 45, "USA", 9.0, "the_wire.jpg");
		restTemplate.put("/series/1", series);
		Series updatedSeries = restTemplate.getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getTitle()).isEqualTo(series.getTitle());
		assertThat(updatedSeries.getRelease_year()).isEqualTo(series.getRelease_year());
		assertThat(updatedSeries.getCountry()).isEqualTo(series.getCountry());
		assertThat(updatedSeries.getImdb_rating()).isEqualTo(series.getImdb_rating());
	}

	@Test
	@DirtiesContext
	void updateSeriesWithRelationships() {
		Genre[] genres = restTemplate.getForObject("/genres", Genre[].class);
		Cast[] casts = restTemplate.getForObject("/casts", Cast[].class);
		Production[] productions = restTemplate.getForObject("/productions", Production[].class);
		Series series = restTemplate.getForObject("/series/1", Series.class);
		series.addGenre(genres[0]);
		series.addCast(casts[0]);
		series.addProduction(productions[0]);
		restTemplate.put("/series/1", series);
		Series updatedSeries = restTemplate.getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getGenres().contains(genres[0])).isTrue();
		assertThat(updatedSeries.getCasts().contains(casts[0])).isTrue();
		assertThat(updatedSeries.getProductions().contains(productions[0])).isTrue();
		Genre g = restTemplate.getForObject("/genres/" + genres[0].getId(), Genre.class);
		Cast c = restTemplate.getForObject("/casts/" + casts[0].getId(), Cast.class);
		Production p = restTemplate.getForObject("/productions/" + productions[0].getId(), Production.class);
		assertThat(g.getSeries().contains(updatedSeries)).isTrue();
		assertThat(c.getSeries().contains(updatedSeries)).isTrue();
		assertThat(p.getSeries().contains(updatedSeries)).isTrue();
		updatedSeries.getGenres().clear();
		updatedSeries.getCasts().clear();
		updatedSeries.getProductions().clear();
		restTemplate.put("/series/1", updatedSeries);
		updatedSeries = restTemplate.getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getGenres().isEmpty()).isTrue();
		assertThat(updatedSeries.getCasts().isEmpty()).isTrue();
		assertThat(updatedSeries.getProductions().isEmpty()).isTrue();
	}

	@Test
	@DirtiesContext
	void deleteSeries() {
		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/series/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		ResponseEntity<Series> response = restTemplate.getForEntity("/series/1", Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void deleteSeriesWithRelationships() {
		Series series = restTemplate.getForObject("/series/3", Series.class);
		Set<Genre> genres = series.getGenres();
		Set<Cast> casts = series.getCasts();
		Set<Production> productions = series.getProductions();
		restTemplate.delete("/series/3");
		ResponseEntity<Series> response = restTemplate.getForEntity("/series/3", Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		for (Genre genre : genres) {
			Genre g = restTemplate.getForObject("/genres/" + genre.getId(), Genre.class);
			assertThat(g.getSeries().contains(series)).isFalse();
		}
		for (Cast cast : casts) {
			Cast c = restTemplate.getForObject("/casts/" + cast.getId(), Cast.class);
			assertThat(c.getSeries().contains(series)).isFalse();
		}
		for (Production production : productions) {
			Production p = restTemplate.getForObject("/productions/" + production.getId(), Production.class);
			assertThat(p.getSeries().contains(series)).isFalse();
		}
	}
}
