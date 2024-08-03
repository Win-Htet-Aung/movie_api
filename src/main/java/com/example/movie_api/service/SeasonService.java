package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;
import com.example.movie_api.repository.SeasonRepository;

@Service
public class SeasonService {
    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesService seriesService;

    public Iterable<Season> getSeasonList() {
        return seasonRepository.findAll();
    }

    public Season getSeason(Long id) {
        return seasonRepository.findById(id).get();
    }

    public Season createSeason(Long seriesId, Season season) {
        Series series = seriesService.getSeries(seriesId);
        season.setSeries(series);
        return seasonRepository.save(season);
    }
}
