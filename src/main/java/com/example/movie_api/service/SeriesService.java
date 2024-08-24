package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Review;
import com.example.movie_api.model.Series;
import com.example.movie_api.model.User;
import com.example.movie_api.repository.ReviewRepository;
import com.example.movie_api.repository.SeriesRepository;
import com.example.movie_api.repository.UserRepository;
import com.example.movie_api.utils.SearchCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager em;

    public Iterable<Series> getSeriesList() {
        return seriesRepository.findAll();
    }

    public Series getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId).get();
    }

    public Series createSeries(Series newSeries) {
        return seriesRepository.save(newSeries);
    }

    public void updateSeries(Long seriesId, Series updatedSeries) {
        Series series = getSeries(seriesId);
        updatedSeries.setId(series.getId());
        seriesRepository.save(updatedSeries);
    }

    public void deleteSeries(Long seriesId) {
        seriesRepository.deleteById(seriesId);
    }

    public Iterable<Series> searchSeries(SearchCriteria searchCriteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Series> cq = cb.createQuery(Series.class);
        Root<Series> series = cq.from(Series.class);
        cq.where(
            cb.and(
                searchCriteria.getTitlePredicate(cb, series),
                searchCriteria.getGenrePredicate(Series.class, cb, cq, series),
                searchCriteria.getImdbRatingPredicate(cb, series)
            )
        );
        TypedQuery<Series> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public Review createReview(Long seriesId, Review newReview, String username) {
        Series series = getSeries(seriesId);
        User user = userRepository.findByUsername(username);
        newReview.setUser(user);
        newReview.getSeries().add(series);
        Review savedReview = reviewRepository.save(newReview);
        if (series.getReviewCount() == 0) {
            series.setUserRating(Double.valueOf(savedReview.getRating()));
        } else {
            Double total = series.getUserRating() * series.getReviewCount() + savedReview.getRating();
            series.setUserRating(total / (series.getReviewCount() + 1));
        }
        series.addReview(savedReview);
        seriesRepository.save(series);
        return savedReview;
    }
}
