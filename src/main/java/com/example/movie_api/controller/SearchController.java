package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.dto.MovieSeries;
import com.example.movie_api.service.SearchService;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public Page<MovieSeries> getSearchPage(@RequestParam("query") String query, Pageable pageable) {
        return searchService.getSearchPage(query, pageable);
    }
}
