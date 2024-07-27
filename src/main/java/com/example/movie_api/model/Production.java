package com.example.movie_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productions")
public class Production {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Production(String name) {
        this.name = name;
    }

    public Production() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
