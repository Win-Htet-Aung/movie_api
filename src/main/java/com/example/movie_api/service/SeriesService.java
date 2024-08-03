package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Series;
import com.example.movie_api.repository.SeriesRepository;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    public Iterable<Series> getSeriesList() {
        return seriesRepository.findAll();
    }
}
