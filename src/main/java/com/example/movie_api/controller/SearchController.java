package com.example.movie_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.dto.MovieSeries;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.service.GenreService;
import com.example.movie_api.service.SearchService;
import com.example.movie_api.utils.SearchCriteria;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private EntityManager em;

    @GetMapping("/search")
    public Page<MovieSeries> getSearchPage(
        @RequestParam("query") String query, Pageable pageable,
        SearchCriteria searchCriteria
    ) {
        String[] genres = searchCriteria.getGenre();
        System.out.println();
        Iterable<Genre> filteredGenres = genreService.getGenreByNames(genres);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);
        List<Predicate> predicates = new ArrayList<>();

        for (String g : genres) {
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
        cq.where(
            cb.and(
                cb.like(movie.get("title"), "%" + query + "%"),
                cb.or(predicates.toArray(new Predicate[predicates.size()]))
            )
        );
        TypedQuery<Movie> tq = em.createQuery(cq);
        List<Movie> filteredMovies = tq.getResultList();
        for (Movie m : filteredMovies) {
            System.out.println();
            System.out.println(m.getTitle());
            System.out.println(m.getGenres());
            System.out.println();
        }
        return searchService.getSearchPage(query, pageable);
    }
}
