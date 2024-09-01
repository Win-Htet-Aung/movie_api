package com.example.movie_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.movie_api.model.Role;
import com.example.movie_api.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoleTests extends MovieApiApplicationTests {
    
    @Test
    public void getRoleList() {
        ResponseEntity<Role[]> response = authRT("user").getForEntity("/roles", Role[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    public void createRole() {
        Role role = new Role("Director");
        URI new_role_location = authRT("admin").postForLocation("/roles", role);
        ResponseEntity<Role> response = authRT("user").getForEntity(new_role_location, Role.class);
        assertThat(response.getBody().getName()).isEqualTo("Director");
        assertThat(response.getBody().getUsers()).isEmpty();
    }

    @Test
    @DirtiesContext
    public void updateRole() {
        Role role = new Role("Director");
        authRT("admin").put("/roles/2", role);
        ResponseEntity<Role> response = authRT("user").getForEntity("/roles/2", Role.class);
        assertThat(response.getBody().getName()).isEqualTo("Director");
        assertThat(response.getBody().getUsers().size()).isEqualTo(1);
        role = new Role("Actor");
        ResponseEntity<Void> nf_response = authRT("admin").exchange("/roles/10", HttpMethod.PUT, new HttpEntity<>(role), Void.class);
        assertThat(nf_response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void deleteRole() {
        Role role = authRT("user").getForObject("/roles/1", Role.class);
        Set<User> users = role.getUsers();
        authRT("admin").delete("/roles/1");
        ResponseEntity<Role> response = authRT("user").getForEntity("/roles/1", Role.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        for (User user : users) {
            ResponseEntity<User> uResp = authRT("user").getForEntity("/users/" + user.getId(), User.class);
            assertThat(uResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
        authRT("admin").delete("/roles/3");
        response = authRT("user").getForEntity("/roles/3", Role.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
