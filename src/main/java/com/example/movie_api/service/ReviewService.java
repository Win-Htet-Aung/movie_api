package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Episode;
import com.example.movie_api.model.Movie;
import com.example.movie_api.model.Review;
import com.example.movie_api.model.Season;
import com.example.movie_api.model.Series;
import com.example.movie_api.repository.EpisodeRepository;
import com.example.movie_api.repository.MovieRepository;
import com.example.movie_api.repository.ReviewRepository;
import com.example.movie_api.repository.SeasonRepository;
import com.example.movie_api.repository.SeriesRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    public Page<Review> getReviewList(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Review getReview(Long id) {
        return reviewRepository.findById(id).get();
    }

    public void deleteReview(Long id) {
        Review review = getReview(id);
        for (Movie m : review.getMovies()) {
            m.removeReview(review);
            movieRepository.save(m);
        }
        for (Series s : review.getSeries()) {
            s.removeReview(review);
            seriesRepository.save(s);
        }
        for (Season s : review.getSeasons()) {
            s.removeReview(review);
            seasonRepository.save(s);
        }
        for (Episode e : review.getEpisodes()) {
            e.removeReview(review);
            episodeRepository.save(e);
        }
        reviewRepository.deleteById(id);
    }
}
