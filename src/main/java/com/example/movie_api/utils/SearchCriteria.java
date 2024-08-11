package com.example.movie_api.utils;

public class SearchCriteria {
    private String query;
    private String[] genre = new String[]{};
    private boolean allGenre = false;

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
}
