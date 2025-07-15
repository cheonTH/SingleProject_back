package com.single.map.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.single.map.dto.CommentDTO;
import com.single.map.model.CommentEntity;
import com.single.map.model.UserEntity;
import com.single.map.repository.CommentRepository;
import com.single.map.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;

    public CommentDTO save(CommentDTO dto) {
        CommentEntity entity = CommentEntity.builder()
                .boardId(dto.getBoardId())
                .userId(dto.getUserId())
                .content(dto.getContent())
                .parentId(dto.getParentId())
                .createdTime(LocalDateTime.now().toString())
                .updatedTime(LocalDateTime.now().toString())
                .build();

        repository.save(entity);

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
        repository.deleteById(id);
    }

    public void update(Long id, String newContent) {
        CommentEntity comment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setContent(newContent);
        comment.setUpdatedTime(LocalDateTime.now().toString());
        repository.save(comment);
    }
}
