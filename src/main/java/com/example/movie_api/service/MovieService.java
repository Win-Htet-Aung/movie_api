package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        cq.where(
            cb.and(
                searchCriteria.getTitlePredicate(cb, movie),
                searchCriteria.getGenrePredicate(Movie.class, cb, cq, movie),
                searchCriteria.getImdbRatingPredicate(cb, movie)
            )
        );
        TypedQuery<Movie> tq = em.createQuery(cq);
        return tq.getResultList();
    }
}
