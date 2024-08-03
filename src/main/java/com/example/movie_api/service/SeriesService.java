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

    public Series getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId).get();
    }

    public Series createSeries(Series newSeries) {
        return seriesRepository.save(newSeries);
    }

    public void updateSeries(Long seriesId, Series updatedSeries) {
        Series series = getSeries(seriesId);
        updatedSeries.setId(series.getId());
        seriesRepository.save(updatedSeries);
    }

    public void deleteSeries(Long seriesId) {
        seriesRepository.deleteById(seriesId);
    }
}
