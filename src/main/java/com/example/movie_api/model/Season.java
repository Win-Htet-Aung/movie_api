package com.example.movie_api.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "season_number")
    private Integer season_number;

    @Column(name = "summary")
    private String summary;

    @Column(name = "release_year")
    private Integer release_year;

    @Column(name = "imdb_rating")
    private Double imdb_rating;

    @Column(name = "user_rating")
    private Double userRating = 0.0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    @JsonIgnoreProperties(value = {"seasons"}, allowSetters = true)
    private Series series;

    @OneToMany(mappedBy = "season", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value =  {"season"}, allowSetters = true)
    private Set<Episode> episodes = new HashSet<Episode>();

    @ManyToMany
    @JoinTable(
        name = "season_review",
        joinColumns = @JoinColumn(name="season_id"),
        inverseJoinColumns = @JoinColumn(name="review_id")
    )
    @JsonIgnoreProperties({"movies", "series", "seasons", "episodes"})
    private Set<Review> reviews = new HashSet<Review>();

    public Season(Integer season_number, String summary, Integer release_year, Double imdb_rating) {
        this.season_number = season_number;
        this.summary = summary;
        this.release_year = release_year;
        this.imdb_rating = imdb_rating;
    }

    public Season() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeason_number() {
        return season_number;
    }

    public void setSeason_number(Integer season_number) {
        this.season_number = season_number;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getRelease_year() {
        return release_year;
    }

    public void setRelease_year(Integer release_year) {
        this.release_year = release_year;
    }

    public Double getImdb_rating() {
        return imdb_rating;
    }

    public void setImdb_rating(Double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Set<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Set<Episode> episodes) {
        this.episodes = episodes;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Season other = (Season) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void addReview(Review review) {
        reviews.add(review);
        reviewCount++;
    }

    public void removeReview(Review review) {
        if (reviewCount == 1) {
            userRating = 0.0;
        } else {
            Double total = userRating * reviewCount - review.getRating();
            userRating = total / (reviewCount - 1);
        }
        reviews.remove(review);
        reviewCount--;
    }
}
