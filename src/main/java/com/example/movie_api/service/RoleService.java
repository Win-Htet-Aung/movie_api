package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Role;
import com.example.movie_api.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> getRoleList() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).get();
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public void updateRole(Long id, Role updateRole) {
        Role role = getRole(id);
        updateRole.setId(role.getId());
        roleRepository.save(updateRole);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
