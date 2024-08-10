package com.example.movie_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.movie_api.model.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    public Genre findByName(String name);

    @Query("select g from Genre g where g.name in ?1")
    public Iterable<Genre> findByNameInIterable(String[] names);
}
