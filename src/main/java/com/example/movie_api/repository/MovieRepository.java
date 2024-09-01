package com.example.movie_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.movie_api.model.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long>, PagingAndSortingRepository<Movie, Long> {
    Page<Movie> findAll(Pageable pageable);
    Iterable<Movie> findByTitleContaining(String title);
    Movie findBySlug(String slug);
}
