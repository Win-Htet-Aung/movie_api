package com.example.movie_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "casts")
public class Cast {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Cast(String name) {
        this.name = name;
    }

    public Cast() {
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
