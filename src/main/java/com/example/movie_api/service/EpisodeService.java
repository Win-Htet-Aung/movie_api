package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Season;
import com.example.movie_api.repository.EpisodeRepository;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonService seasonService;

    public Iterable<Episode> getEpisodes() {
        return episodeRepository.findAll();
    }

    public Episode getEpisode(Long episodeId) {
        return episodeRepository.findById(episodeId).get();
    }

    public Episode createEpisode(Long seasonId, Episode episode) {
        Season season = seasonService.getSeason(seasonId);
        episode.setSeason(season);
        return episodeRepository.save(episode);
    }

    public void updateEpisode(Long episodeId, Episode episode) {
        Episode oldEpisode = getEpisode(episodeId);
        if (episode.getSeason() == null) {
            episode.setSeason(oldEpisode.getSeason());
        }
        if (episode.getReviews() == null) {
            episode.setReviews(oldEpisode.getReviews());
        }
        episode.setId(oldEpisode.getId());
        episodeRepository.save(episode);
    }

    public void deleteEpisode(Long id) {
        Episode episode = getEpisode(id);
        Season season = episode.getSeason();
        season.getEpisodes().remove(episode);
        seasonService.updateSeason(season.getId(), season);
        episodeRepository.deleteById(id);
    }
}
