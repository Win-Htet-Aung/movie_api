package com.example.movie_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieApiApplicationTests {
    @Autowired
    protected TestRestTemplate restTemplate;

    protected TestRestTemplate authRT() {
		return restTemplate.withBasicAuth("admin", "admin123");
	}
}
