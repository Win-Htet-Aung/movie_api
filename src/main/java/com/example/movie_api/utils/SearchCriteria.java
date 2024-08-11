package com.example.movie_api.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.movie_api.model.Genre;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class SearchCriteria {
    private String query;
    private String[] genre = new String[]{};
    private boolean allGenre = false;
    private String[] imdbRating;

    public SearchCriteria() {
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isAllGenre() {
        return allGenre;
    }

    public void setAllGenre(boolean allGenre) {
        this.allGenre = allGenre;
    }

    public String[] getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String[] imdbRating) {
        this.imdbRating = imdbRating;
    }

    public <T> Predicate getTitlePredicate(CriteriaBuilder cb, Root<T> root) {
        return cb.like(root.get("title"), "%" + getQuery() + "%");
    }

    public <T> Predicate getGenrePredicate(Class<T> cls, CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        for (String g : getGenre()) {
            // Initialize the subquery
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<T> subqueryT = subquery.from(cls);
            Join<Genre, T> subqueryGenre = subqueryT.join("genres");

            // Select the Movie ID where one of their genres matches
            subquery.select(subqueryT.get("id")).where(
                cb.equal(subqueryGenre.get("name"), g)
            );

            // Filter by Movies that match one of the Movies found in the subquery
            predicates.add(cb.in(root.get("id")).value(subquery));
        }
        
        // Use all predicates above to query
        if (predicates.size() > 0) {
            if (allGenre) {
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } else {
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return cb.and();
    }

    public <T> Predicate getImdbRatingPredicate(CriteriaBuilder cb, Root<T> root) {
        Double value = Double.parseDouble(imdbRating[0]);
        String operator = imdbRating[1];
        if (operator.equals("ge")) {
            return cb.ge(root.get("imdbRating"), value);
        } else {
            return cb.le(root.get("imdbRating"), value);
        }
    }
}
