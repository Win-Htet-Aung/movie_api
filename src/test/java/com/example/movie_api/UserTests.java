package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.dto.UserRequest;
import com.example.movie_api.model.Role;
import com.example.movie_api.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests extends MovieApiApplicationTests {

    @Test
    public void getUserList() {
        ResponseEntity<User[]> response = authRT().getForEntity("/users", User[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void createUser() {
        UserRequest user = new UserRequest("testuser", "Test User", "testuser@example.com", "testpassword");
        URI new_user_location = authRT().postForLocation("/users", user);
        ResponseEntity<User> response = authRT().getForEntity(new_user_location, User.class);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getDisplayname()).isEqualTo("Test User");
        assertThat(response.getBody().getRole().getName()).isEqualTo("user");
    }

    @Test
    @DirtiesContext
    public void updateUser() {
        UserRequest user = new UserRequest("testuser", "Test User", "testuser@example.com", "testpassword");
        authRT().put("/users/2", user);
        ResponseEntity<User> response = authRT().getForEntity("/users/2", User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getDisplayname()).isEqualTo("Test User");
        Role[] roles = authRT().getForObject("/roles", Role[].class);
        user.setRole(roles[0]);
        authRT().put("/users/2", user);
        response = authRT().getForEntity("/users/2", User.class);
        assertThat(response.getBody().getRole().getName()).isEqualTo("admin");
    }

    @Test
    @DirtiesContext
    public void updateUserNotFound() {
        User user = new User("testuser", "Test User", "testuser@example.com", "testpassword");
        ResponseEntity<Void> nf_response = authRT().exchange("/users/10", HttpMethod.PUT, new HttpEntity<>(user), Void.class);
        assertThat(nf_response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void deleteUser() {
        User user = authRT().getForObject("/users/2", User.class);
        Role role = user.getRole();
        authRT().delete("/users/2");
        ResponseEntity<User> response = authRT().getForEntity("/users/2", User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        role = authRT().getForObject("/roles/" + role.getId(), Role.class);
        assertThat(role.getUsers().size()).isEqualTo(0);
    }
}
