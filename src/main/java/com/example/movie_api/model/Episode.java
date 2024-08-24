package com.example.movie_api.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "episode_number")
    private Integer episode_number;

    @Column(name = "title")
    private String title;

    @Column(name = "air_date")
    private Date air_date;

    @Column(name = "imdb_rating")
    private Double imdb_rating;

    @Column(name = "user_rating")
    private Double userRating = 0.0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @ManyToMany
    @JoinTable(
        name = "episode_review",
        joinColumns = @JoinColumn(name="episode_id"),
        inverseJoinColumns = @JoinColumn(name="review_id")
    )
    @JsonIgnoreProperties({"movies", "series", "seasons", "episodes"})
    private Set<Review> reviews = new HashSet<Review>();

    public Episode(Integer episode_number, String title, Date air_date, Double imdb_rating) {
        this.episode_number = episode_number;
        this.title = title;
        this.air_date = air_date;
        this.imdb_rating = imdb_rating;
    }

    public Episode() {
    }

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    @JsonIgnoreProperties(value = {"episodes"}, allowSetters = true)
    private Season season;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(Integer episode_number) {
        this.episode_number = episode_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAir_date() {
        return air_date;
    }

    public void setAir_date(Date air_date) {
        this.air_date = air_date;
    }

    public Double getImdb_rating() {
        return imdb_rating;
    }

    public void setImdb_rating(Double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
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
        Episode other = (Episode) obj;
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
}
