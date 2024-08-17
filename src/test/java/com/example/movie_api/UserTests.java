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
import com.example.movie_api.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUserList() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void createUser() {
        User user = new User("testuser", "Test User", "testuser@example.com", "testpassword");
        URI new_user_location = restTemplate.postForLocation("/users", user);
        ResponseEntity<User> response = restTemplate.getForEntity(new_user_location, User.class);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getDisplayname()).isEqualTo("Test User");
        assertThat(response.getBody().getRole().getName()).isEqualTo("user");
    }

    @Test
    @DirtiesContext
    public void updateUser() {
        User user = new User("testuser", "Test User", "testuser@example.com", "testpassword");
        restTemplate.put("/users/1", user);
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getDisplayname()).isEqualTo("Test User");
        Role[] roles = restTemplate.getForObject("/roles", Role[].class);
        user.setRole(roles[1]);
        restTemplate.put("/users/1", user);
        response = restTemplate.getForEntity("/users/1", User.class);
        assertThat(response.getBody().getRole().getName()).isEqualTo("user");
    }

    @Test
    @DirtiesContext
    public void updateUserNotFound() {
        User user = new User("testuser", "Test User", "testuser@example.com", "testpassword");
        ResponseEntity<Void> nf_response = restTemplate.exchange("/users/10", HttpMethod.PUT, new HttpEntity<>(user), Void.class);
        assertThat(nf_response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void deleteUser() {
        User user = restTemplate.getForObject("/users/1", User.class);
        Role role = user.getRole();
        restTemplate.delete("/users/1");
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        role = restTemplate.getForObject("/roles/" + role.getId(), Role.class);
        assertThat(role.getUsers().size()).isEqualTo(0);
    }
}
