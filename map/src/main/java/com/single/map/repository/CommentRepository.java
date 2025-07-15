package com.single.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.single.map.model.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardIdOrderByCreatedTimeAsc(Long boardId);
}
