package com.example.movie_api.controller;

import java.net.URI;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.movie_api.model.Role;
import com.example.movie_api.service.RoleService;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<Iterable<Role>> getRoleList() {
        return ResponseEntity.ok(roleService.getRoleList());
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Role> getRole(@PathVariable Long roleId) {
        Role role;
        try {
            role = roleService.getRole(roleId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }

    @PostMapping("/roles")
    public ResponseEntity<Void> createRole(@RequestBody Role role) {
        Role created_role = roleService.createRole(role);
        URI new_role_location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{roleId}")
                .buildAndExpand(created_role.getId())
                .toUri();
        return ResponseEntity.created(new_role_location).build();
    }

    @PutMapping("/roles/{roleId}")
    public ResponseEntity<Void> updateRole(@PathVariable Long roleId, @RequestBody Role role) {
        try {
            roleService.updateRole(roleId, role);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
