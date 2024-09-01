package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Cast;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Production;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SeriesTests extends MovieApiApplicationTests {

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
		URI new_series_location = authRT("admin").postForLocation("/series", series, Void.class);
		ResponseEntity<Series> response = restTemplate.getForEntity(new_series_location, Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(series.getTitle());
		assertThat(response.getBody().getReleaseYear()).isEqualTo(series.getReleaseYear());
		assertThat(response.getBody().getCountry()).isEqualTo(series.getCountry());
		assertThat(response.getBody().getImdbRating()).isEqualTo(series.getImdbRating());
	}

	@Test
	@DirtiesContext
	void createSeriesWithRelationships() {
		Genre[] genres = authRT("user").getForObject("/genres", Genre[].class);
		Cast[] casts = authRT("user").getForObject("/casts", Cast[].class);
		Production[] productions = authRT("user").getForObject("/productions", Production[].class);
		Series series = new Series("The Wire", "An American news satire and cable drama series.", 2002, 45, "USA", 9.0, "the_wire.jpg");
		series.addGenre(genres[0]);
		series.addGenre(genres[1]);
		series.addCast(casts[0]);
		series.addCast(casts[1]);
		series.addProduction(productions[0]);
		series.addProduction(productions[1]);
		URI new_series_location = authRT("admin").postForLocation("/series", series, Void.class);
		ResponseEntity<Series> response = authRT("user").getForEntity(new_series_location, Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getGenres().contains(genres[0])).isTrue();
		assertThat(response.getBody().getGenres().contains(genres[1])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[0])).isTrue();
		assertThat(response.getBody().getCasts().contains(casts[1])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[0])).isTrue();
		assertThat(response.getBody().getProductions().contains(productions[1])).isTrue();
	}

	@Test
	@DirtiesContext
	void updateSeries() {
		Series series = new Series("The Wire", "An American news satire and cable drama series.", 2002, 45, "USA", 9.0, "the_wire.jpg");
		authRT("admin").put("/series/1", series);
		Series updatedSeries = authRT("user").getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getTitle()).isEqualTo(series.getTitle());
		assertThat(updatedSeries.getReleaseYear()).isEqualTo(series.getReleaseYear());
		assertThat(updatedSeries.getCountry()).isEqualTo(series.getCountry());
		assertThat(updatedSeries.getImdbRating()).isEqualTo(series.getImdbRating());
	}

	@Test
	@DirtiesContext
	void updateSeriesWithRelationships() {
		Genre[] genres = authRT("user").getForObject("/genres", Genre[].class);
		Cast[] casts = authRT("user").getForObject("/casts", Cast[].class);
		Production[] productions = authRT("user").getForObject("/productions", Production[].class);
		Series series = authRT("user").getForObject("/series/1", Series.class);
		series.addGenre(genres[0]);
		series.addCast(casts[0]);
		series.addProduction(productions[0]);
		authRT("admin").put("/series/1", series);
		Series updatedSeries = authRT("user").getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getGenres().contains(genres[0])).isTrue();
		assertThat(updatedSeries.getCasts().contains(casts[0])).isTrue();
		assertThat(updatedSeries.getProductions().contains(productions[0])).isTrue();		
		updatedSeries.getGenres().clear();
		updatedSeries.getCasts().clear();
		updatedSeries.getProductions().clear();
		authRT("admin").put("/series/1", updatedSeries);
		updatedSeries = authRT("user").getForObject("/series/1", Series.class);
		assertThat(updatedSeries.getGenres().isEmpty()).isTrue();
		assertThat(updatedSeries.getCasts().isEmpty()).isTrue();
		assertThat(updatedSeries.getProductions().isEmpty()).isTrue();
	}

	@Test
	@DirtiesContext
	void deleteSeries() {
		ResponseEntity<Void> deleteResponse = authRT("admin").exchange("/series/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		ResponseEntity<Series> response = authRT("user").getForEntity("/series/1", Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void deleteSeriesWithRelationships() {
		Series series = authRT("user").getForObject("/series/3", Series.class);
		Set<Genre> genres = series.getGenres();
		Set<Cast> casts = series.getCasts();
		Set<Production> productions = series.getProductions();
		Set<Season> seasons = series.getSeasons();
		authRT("admin").delete("/series/3");
		ResponseEntity<Series> response = authRT("user").getForEntity("/series/3", Series.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		for (Genre genre : genres) {
			Genre g = authRT("user").getForObject("/genres/" + genre.getId(), Genre.class);
			assertThat(g.getSeries().contains(series)).isFalse();
		}
		for (Cast cast : casts) {
			Cast c = authRT("user").getForObject("/casts/" + cast.getId(), Cast.class);
			assertThat(c.getSeries().contains(series)).isFalse();
		}
		for (Production production : productions) {
			Production p = authRT("user").getForObject("/productions/" + production.getId(), Production.class);
			assertThat(p.getSeries().contains(series)).isFalse();
		}
		for (Season season : seasons) {
			ResponseEntity<Season> sr = authRT("user").getForEntity("/seasons/" + season.getId(), Season.class);
			assertThat(sr.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
}
