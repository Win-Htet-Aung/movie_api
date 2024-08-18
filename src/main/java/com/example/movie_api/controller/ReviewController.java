package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.model.Review;
import com.example.movie_api.service.ReviewService;

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/reviews")
    public ResponseEntity<Page<Review>> getReviewList(Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewList(pageable));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }
}
