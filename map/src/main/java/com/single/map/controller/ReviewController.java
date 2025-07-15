package com.single.map.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.single.map.dto.ReviewDTO;
import com.single.map.model.ReviewEntity;
import com.single.map.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ✅ 리뷰 등록
    @PostMapping
    public ResponseEntity<?> writeReview(@RequestBody ReviewDTO dto) {
        ReviewEntity saved = reviewService.save(dto);
        return ResponseEntity.ok().body(saved);
    }

    // ✅ 특정 장소의 리뷰 목록 조회
    @GetMapping("/{placeId}")
    public ResponseEntity<?> getReviewsByPlace(@PathVariable String placeId) {
        List<ReviewDTO> reviews = reviewService.findByPlaceId(placeId);
        return ResponseEntity.ok().body(reviews);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.ok().body("리뷰 삭제 완료");
    }
}
