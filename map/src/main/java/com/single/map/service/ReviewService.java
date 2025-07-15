package com.single.map.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.single.map.dto.ReviewDTO;
import com.single.map.model.ReviewEntity;
import com.single.map.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewEntity save(ReviewDTO dto) {
        ReviewEntity entity = ReviewEntity.builder()
                .placeId(dto.getPlaceId())
                .placeName(dto.getPlaceName())
                .review(dto.getReview())
                .nickName(dto.getNickName())
                .userId(dto.getUserId())  // ✅ 추가
                .build();
        return reviewRepository.save(entity);
    }


    public List<ReviewDTO> findByPlaceId(String placeId) {
        return reviewRepository.findByPlaceId(placeId).stream()
        		.map(entity -> ReviewDTO.builder()
        		        .id(entity.getId())
        		        .placeId(entity.getPlaceId())
        		        .placeName(entity.getPlaceName())
        		        .review(entity.getReview())
        		        .nickName(entity.getNickName())
        		        .userId(entity.getUserId())  // ✅ 추가
        		        .build())
                .collect(Collectors.toList());
    }
    
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
