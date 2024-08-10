package com.example.movie_api.utils;

public class SearchCriteria {
    private String[] genre;
    private boolean genre_mode;

    public String[] getGenre() {
        return genre;
    }

    public SearchCriteria() {
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }
    
}
