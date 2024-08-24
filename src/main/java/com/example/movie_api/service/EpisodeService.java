package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Review;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.User;
import com.example.movie_api.repository.EpisodeRepository;
import com.example.movie_api.repository.ReviewRepository;
import com.example.movie_api.repository.UserRepository;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Review createReview(Long episodeId, Review review, String username) {
        Episode episode = getEpisode(episodeId);
        User user = userRepository.findByUsername(username);
        review.setUser(user);
        review.getEpisodes().add(episode);
        Review savedReview = reviewRepository.save(review);
        if (episode.getReviewCount() == 0) {
            episode.setUserRating(Double.valueOf(savedReview.getRating()));
        } else {
            Double total = episode.getUserRating() * episode.getReviewCount() + savedReview.getRating();
            episode.setUserRating(total / (episode.getReviewCount() + 1));
        }
        episode.addReview(savedReview);
        episodeRepository.save(episode);
        return savedReview;
    }
}
