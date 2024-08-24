package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Review;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;
import com.example.movie_api.model.User;
import com.example.movie_api.repository.ReviewRepository;
import com.example.movie_api.repository.SeasonRepository;
import com.example.movie_api.repository.UserRepository;

@Service
public class SeasonService {
    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

    public Review createReview(Long seasonId, Review review, String username) {
        Season season = getSeason(seasonId);
        User user = userRepository.findByUsername(username);
        review.setUser(user);
        review.getSeasons().add(season);
        Review savedReview = reviewRepository.save(review);
        if (season.getReviewCount() == 0) {
            season.setUserRating(Double.valueOf(savedReview.getRating()));
        } else {
            Double total = season.getUserRating() * season.getReviewCount() + savedReview.getRating();
            season.setUserRating(total / (season.getReviewCount() + 1));
        }
        season.addReview(savedReview);
        seasonRepository.save(season);
        return savedReview;
    }
}
