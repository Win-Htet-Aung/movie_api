package com.example.movie_api.dto;

import com.example.movie_api.model.Role;

public class UserRequest {
    private String username;
    private String displayname;
    private String email;
    private String password;
    private Role role;
    
    public UserRequest(String username, String displayname, String email, String password) {
        this.username = username;
        this.displayname = displayname;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
