package com.example.movie_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.movie_api.dto.MovieSeries;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Series;
import com.example.movie_api.utils.SearchCriteria;

@Service
public class SearchService {
    @Autowired
    private MovieService moviceService;
    
    @Autowired
    private SeriesService seriesService;

    public Page<MovieSeries> getSearchPage(SearchCriteria searchCriteria, Pageable pageable) {
        Iterable<Movie> movies = moviceService.searchMovies(searchCriteria);
        Iterable<Series> series = seriesService.searchSeries(searchCriteria);
        List<MovieSeries> movies_series = new ArrayList<>();
        for (Movie movie : movies) {
            movies_series.add(new MovieSeries(
                movie.getId(), movie.getTitle(), movie.getSummary(), movie.getReleaseYear(),
                movie.getDuration(), movie.getCountry(), movie.getImdbRating(), movie.getCover(),
                "movie"
            ));
        }

        for (Series s : series) {
            movies_series.add(new MovieSeries(
                s.getId(), s.getTitle(), s.getSummary(), s.getReleaseYear(),
                s.getDuration(), s.getCountry(), s.getImdbRating(), s.getCover(),
                "series"
            ));
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), movies_series.size());
        return new PageImpl<>(movies_series.subList(start, end), pageable, movies_series.size());
    }
}
