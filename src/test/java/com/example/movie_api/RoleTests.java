package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

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

    @Test
    @DirtiesContext
    public void createRole() {
        Role role = new Role("Director");
        URI new_role_location = restTemplate.postForLocation("/roles", role);
        ResponseEntity<Role> response = restTemplate.getForEntity(new_role_location, Role.class);
        assertThat(response.getBody().getName()).isEqualTo("Director");
        assertThat(response.getBody().getUsers()).isEmpty();
    }

    @Test
    @DirtiesContext
    public void updateRole() {
        Role role = new Role("Director");
        restTemplate.put("/roles/1", role);
        ResponseEntity<Role> response = restTemplate.getForEntity("/roles/1", Role.class);
        assertThat(response.getBody().getName()).isEqualTo("Director");
        assertThat(response.getBody().getUsers().size()).isEqualTo(1);
        role = new Role("Actor");
        ResponseEntity<Void> nf_response = restTemplate.exchange("/roles/10", HttpMethod.PUT, new HttpEntity<>(role), Void.class);
        assertThat(nf_response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void deleteRole() {
        restTemplate.delete("/roles/1");
        ResponseEntity<Role> response = restTemplate.getForEntity("/roles/1", Role.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
