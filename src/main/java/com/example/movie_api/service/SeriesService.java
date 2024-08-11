package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Series;
import com.example.movie_api.repository.SeriesRepository;
import com.example.movie_api.utils.SearchCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

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
}
