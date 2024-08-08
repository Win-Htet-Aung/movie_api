package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
