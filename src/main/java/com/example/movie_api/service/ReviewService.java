package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Review;
import com.example.movie_api.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> getReviewList(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Review getReview(Long id) {
        return reviewRepository.findById(id).get();
    }
}
