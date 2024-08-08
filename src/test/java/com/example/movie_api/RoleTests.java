package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.movie_api.model.Role;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoleTests {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void getRoleList() {
        ResponseEntity<Role[]> response = restTemplate.getForEntity("/roles", Role[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);
    }
}
