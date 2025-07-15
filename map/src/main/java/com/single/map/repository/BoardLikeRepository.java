package com.single.map.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.single.map.model.BoardLikeEntity;

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Long> {
    
    Optional<BoardLikeEntity> findByBoardIdAndUserId(Long boardId, String userId);

    List<BoardLikeEntity> findByUserId(String userId);

    Long countByBoardId(Long boardId);

    void deleteByBoardIdAndUserId(Long boardId, String userId);
}