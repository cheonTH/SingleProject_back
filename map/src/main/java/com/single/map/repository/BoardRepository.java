package com.single.map.repository;

import com.single.map.model.BoardEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByCategory(String category);
    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity b SET b.commentCount = :count WHERE b.id = :boardId")
    void updateCommentCount(@Param("boardId") Long boardId, @Param("count") int count);
}
