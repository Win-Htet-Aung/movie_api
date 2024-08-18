package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Production;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductionTests extends MovieApiApplicationTests {

    @Test
	void getProductionList() {
		ResponseEntity<Production[]> response = authRT().getForEntity("/productions", Production[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(6);
	}

	@Test
	@DirtiesContext
	void createProduction() {
		Production production = new Production("Star Wars");
		URI new_production_location = authRT().postForLocation("/productions", production, Void.class);
		assertNotNull(new_production_location);
		ResponseEntity<Production> response = authRT().getForEntity(new_production_location, Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("Star Wars");
	}

	@Test
	@DirtiesContext
	void updateProduction() {
		Production production = new Production("Marvel Cinematic Universe");
		authRT().put("/productions/1", production);
		Production updatedProduction = authRT().getForObject("/productions/1", Production.class);
		assertThat(updatedProduction.getName()).isEqualTo("Marvel Cinematic Universe");
	}

	@Test
	void updatedProductionNotFound() {
		Production production = new Production("20th Century Fox");
		authRT().put("/productions/10", production);
		ResponseEntity<Production> response = authRT().getForEntity("/productions/10", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}

	@Test
	@DirtiesContext
	void deleteProduction() {
		ResponseEntity<Production> response = authRT().getForEntity("/productions/1", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ResponseEntity<Void> deleteResponse = authRT().exchange("/productions/1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		response = authRT().getForEntity("/productions/1", Production.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
