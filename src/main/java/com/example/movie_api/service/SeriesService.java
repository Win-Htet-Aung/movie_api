package com.example.movie_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Genre;
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
        List<Predicate> predicates = new ArrayList<>();

        for (String g : searchCriteria.getGenre()) {
            // Initialize the subquery
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Series> subquerySeries = subquery.from(Series.class);
            Join<Genre, Series> subqueryGenre = subquerySeries.join("genres");

            // Select the Series ID where one of their genres matches
            subquery.select(subquerySeries.get("id")).where(
            cb.equal(subqueryGenre.get("name"), g));

            // Filter by Series that match one of the Series found in the subquery
            predicates.add(cb.in(series.get("id")).value(subquery));
        }
        
        // Use all predicates above to query
        if (predicates.size() > 0) {
            if (searchCriteria.isAllGenre()) {
                cq.where(
                    cb.and(
                        cb.like(series.get("title"), "%" + searchCriteria.getQuery() + "%"),
                        cb.and(predicates.toArray(new Predicate[predicates.size()]))
                    )
                );
            } else {
                cq.where(
                    cb.and(
                        cb.like(series.get("title"), "%" + searchCriteria.getQuery() + "%"),
                        cb.or(predicates.toArray(new Predicate[predicates.size()]))
                    )
                );
            }
        } else {
            cq.where(
                cb.like(series.get("title"), "%" + searchCriteria.getQuery() + "%")
            );
        }
        TypedQuery<Series> tq = em.createQuery(cq);
        return tq.getResultList();
    }
}
