package com.example.movie_api.dto;

public class MovieSeries {
    private Long id;
    private String title;
    private String summary;
    private Integer releaseYear;
    private Integer duration;
    private String country;
    private Double imdbRating;
    private String cover;
    private String type;

    public MovieSeries(Long id, String title, String summary, Integer releaseYear, Integer duration, String country,
            Double imdbRating, String cover, String type) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.country = country;
        this.imdbRating = imdbRating;
        this.cover = cover;
        this.type = type;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
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

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
