package com.single.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.single.map.model.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByPlaceId(String placeId); // ✅ 장소 ID로 리뷰 조회
}
