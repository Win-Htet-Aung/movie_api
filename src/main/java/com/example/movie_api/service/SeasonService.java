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

    public void updateSeason(Long seasonId, Season updatedSeason) {
        Season season = getSeason(seasonId);
        updatedSeason.setId(season.getId());
        if (updatedSeason.getSeries() == null) {
            updatedSeason.setSeries(season.getSeries());
        }
        seasonRepository.save(updatedSeason);
    }

    public void deleteSeason(Long id) {
        Season season = getSeason(id);
        Series series = season.getSeries();
        series.getSeasons().remove(season);
        seriesService.updateSeries(series.getId(), series);
        seasonRepository.deleteById(id);
    }
}
