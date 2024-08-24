package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Review;
import com.example.movie_api.model.User;
import com.example.movie_api.repository.MovieRepository;
import com.example.movie_api.repository.ReviewRepository;
import com.example.movie_api.repository.UserRepository;
import com.example.movie_api.utils.SearchCriteria;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

    public Review createReview(Long movieId, Review newReview, String username) {
        User user = userRepository.findByUsername(username);
        Movie movie = getMovie(movieId);
        newReview.setUser(user);
        newReview.getMovies().add(movie);
        Review savedReview = reviewRepository.save(newReview);
        if (movie.getReviewCount() == 0) {
            movie.setUserRating(Double.valueOf(savedReview.getRating()));
        } else {
            Double total = movie.getUserRating() * movie.getReviewCount() + savedReview.getRating();
            movie.setUserRating(total / (movie.getReviewCount() + 1));
        }
        movie.addReview(newReview);
        movieRepository.save(movie);
        return savedReview;
    }
}
