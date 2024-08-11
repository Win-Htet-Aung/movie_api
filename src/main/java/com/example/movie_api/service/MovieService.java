package com.example.movie_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.repository.MovieRepository;
import com.example.movie_api.utils.SearchCriteria;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EntityManager em;

    public Page<Movie> getMovieList(Pageable pageable) {
        return movieRepository.findAll(PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))
        ));
    }

    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId).get();
    }

    public Movie createMovie(Movie newMovie) {
        return movieRepository.save(newMovie);
    }

    public void updateMovie(Long movieId, Movie updatedMovie) {
        Movie movie = getMovie(movieId);
        updatedMovie.setId(movie.getId());
        movieRepository.save(updatedMovie);
    }

    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    public Iterable<Movie> searchMovies(SearchCriteria searchCriteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);
        List<Predicate> predicates = new ArrayList<>();

        for (String g : searchCriteria.getGenre()) {
            // Initialize the subquery
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Movie> subqueryMovie = subquery.from(Movie.class);
            Join<Genre, Movie> subqueryGenre = subqueryMovie.join("genres");

            // Select the Movie ID where one of their genres matches
            subquery.select(subqueryMovie.get("id")).where(
            cb.equal(subqueryGenre.get("name"), g));

            // Filter by Movies that match one of the Movies found in the subquery
            predicates.add(cb.in(movie.get("id")).value(subquery));
        }
        
        // Use all predicates above to query
        if (predicates.size() > 0) {
            if (searchCriteria.isAllGenre()) {
                cq.where(
                    cb.and(
                        cb.like(movie.get("title"), "%" + searchCriteria.getQuery() + "%"),
                        cb.and(predicates.toArray(new Predicate[predicates.size()]))
                    )
                );
            } else {
                cq.where(
                    cb.and(
                        cb.like(movie.get("title"), "%" + searchCriteria.getQuery() + "%"),
                        cb.or(predicates.toArray(new Predicate[predicates.size()]))
                    )
                );
            }
        } else {
            cq.where(
                cb.like(movie.get("title"), "%" + searchCriteria.getQuery() + "%")
            );
        }
        TypedQuery<Movie> tq = em.createQuery(cq);
        return tq.getResultList();
    }
}
