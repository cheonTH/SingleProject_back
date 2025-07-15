package com.single.map.repository;

import com.single.map.model.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {

    // 특정 게시글의 이미지 목록 조회
    List<BoardImageEntity> findByBoardId(Long boardId);

    // 필요 시 삭제, 카운트 등도 추가 가능
    void deleteByBoardId(Long boardId);
}
