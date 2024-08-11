package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.dto.MovieSeries;
import com.example.movie_api.service.SearchService;
import com.example.movie_api.utils.SearchCriteria;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public Page<MovieSeries> getSearchPage(SearchCriteria searchCriteria, Pageable pageable) {
        return searchService.getSearchPage(searchCriteria, pageable);
    }
}
