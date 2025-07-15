package com.single.map.repository;

import com.single.map.model.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByCategory(String category);
}
