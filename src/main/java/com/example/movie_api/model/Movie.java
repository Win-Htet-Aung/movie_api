package com.example.movie_api.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "country")
    private String country;

    @Column(name = "imdb_rating")
    private Double imdbRating;

    @Column(name = "user_rating")
    private Double userRating = 0.0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "cover")
    private String cover;

    @ManyToMany
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(name="movie_id"),
        inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    @JsonIgnoreProperties({"movies", "series"})
    private Set<Genre> genres = new HashSet<Genre>();

    @ManyToMany
    @JoinTable(
        name = "movie_cast",
        joinColumns = @JoinColumn(name="movie_id"),
        inverseJoinColumns = @JoinColumn(name="cast_id")
    )
    @JsonIgnoreProperties({"movies", "series"})
    private Set<Cast> casts = new HashSet<Cast>();

    @ManyToMany
    @JoinTable(
        name = "movie_production",
        joinColumns = @JoinColumn(name="movie_id"),
        inverseJoinColumns = @JoinColumn(name="production_id")
    )
    @JsonIgnoreProperties({"movies", "series"})
    private Set<Production> productions = new HashSet<Production>();

    @ManyToMany
    @JoinTable(
        name = "movie_review",
        joinColumns = @JoinColumn(name="movie_id"),
        inverseJoinColumns = @JoinColumn(name="review_id")
    )
    @JsonIgnoreProperties({"movies", "series", "seasons", "episodes"})
    private Set<Review> reviews = new HashSet<Review>();

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Cast> getCasts() {
        return casts;
    }

    public void setCasts(Set<Cast> casts) {
        this.casts = casts;
    }

    public Set<Production> getProductions() {
        return productions;
    }

    public void setProductions(Set<Production> productions) {
        this.productions = productions;
    }

    public Movie() {
    }

    public Movie(String title, String summary, Integer release_year, Integer duration, String country,
            Double imdb_rating, String cover) {
        this.title = title;
        this.summary = summary;
        this.releaseYear = release_year;
        this.duration = duration;
        this.country = country;
        this.imdbRating = imdb_rating;
        this.cover = cover;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void addCast(Cast cast) {
        this.casts.add(cast);
    }

    public void addProduction(Production production) {
        this.productions.add(production);
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
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
        Movie other = (Movie) obj;
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
