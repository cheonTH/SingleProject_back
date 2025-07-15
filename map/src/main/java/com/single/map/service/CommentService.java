package com.single.map.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.single.map.dto.CommentDTO;
import com.single.map.model.CommentEntity;
import com.single.map.model.UserEntity;
import com.single.map.repository.BoardRepository;
import com.single.map.repository.CommentRepository;
import com.single.map.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentDTO save(CommentDTO dto) {
        CommentEntity entity = CommentEntity.builder()
                .boardId(dto.getBoardId())
                .userId(dto.getUserId())
                .content(dto.getContent())
                .parentId(dto.getParentId())
                .createdTime(dto.getCreatedTime())
                .updatedTime(dto.getUpdatedTime())
                .build();

        repository.save(entity);

        // ✅ 댓글 수 반영
        int commentCount = repository.countByBoardId(dto.getBoardId());
        boardRepository.updateCommentCount(dto.getBoardId(), commentCount);

        String nickName = userRepository.findByUserId(dto.getUserId())
                .map(UserEntity::getNickName)
                .orElse("익명");

        return CommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoardId())
                .userId(entity.getUserId())
                .parentId(entity.getParentId())
                .content(entity.getContent())
                .createdTime(entity.getCreatedTime())
                .updatedTime(entity.getUpdatedTime())
                .nickName(nickName)
                .build();
    }

    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        return repository.findByBoardIdOrderByCreatedTimeAsc(boardId).stream()
                .map(entity -> {
                    String nickName = userRepository.findByUserId(entity.getUserId())
                            .map(UserEntity::getNickName).orElse("익명");

                    return CommentDTO.builder()
                            .id(entity.getId())
                            .boardId(entity.getBoardId())
                            .userId(entity.getUserId())
                            .content(entity.getContent())
                            .parentId(entity.getParentId())
                            .createdTime(entity.getCreatedTime())
                            .updatedTime(entity.getUpdatedTime())
                            .nickName(nickName)
                            .build();
                }).toList();
    }

    public void delete(Long id) {
        CommentEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        repository.deleteById(id);

        // ✅ 삭제 후 댓글 수 갱신
        int commentCount = repository.countByBoardId(entity.getBoardId());
        boardRepository.updateCommentCount(entity.getBoardId(), commentCount);
    }

    public void update(Long id, String newContent) {
        CommentEntity comment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setContent(newContent);
        comment.setUpdatedTime(LocalDateTime.now().toString());
        repository.save(comment);
    }
}
