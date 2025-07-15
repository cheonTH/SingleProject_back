package com.single.map.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.map.dto.BoardDTO;
import com.single.map.model.BoardEntity;
import com.single.map.model.BoardLikeEntity;
import com.single.map.repository.BoardLikeRepository;
import com.single.map.repository.BoardRepository;
import com.single.map.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ✅ JSON 처리용
    private final BoardLikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public BoardEntity save(BoardDTO dto) {
        String imageUrlsJson = "[]";
        try {
            if (dto.getImageUrls() != null) {
                imageUrlsJson = objectMapper.writeValueAsString(dto.getImageUrls());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BoardEntity board = BoardEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .userId(dto.getUserId())
                .nickName(dto.getNickName())
                .writingTime(dto.getWritingTime())
                .imageUrl(imageUrlsJson) // ✅ JSON 문자열 저장
                .category(dto.getCategory())
                .build();

        System.out.println("닉네임 : " + userService.getNickNameByUserId(dto.getUserId()));
        return boardRepository.save(board);
    }

    public List<BoardDTO> findAll() {
        return boardRepository.findAll().stream().map(entity -> {
            String nickname = userService.getNickNameByUserId(entity.getUserId());
            List<String> imageUrls = parseImageUrls(entity.getImageUrl());
            long likeCount = getLikeCount(entity.getId()); // ✅ 실제 좋아요 수 계산

            return BoardDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .userId(entity.getUserId())
                    .nickName(nickname)
                    .writingTime(entity.getWritingTime())
                    .category(entity.getCategory())
                    .likeCount(likeCount) // ✅ 수정됨
                    .isLiked(false)       // ❌ 이건 userId가 없으니 false로
                    .isSaved(entity.isSaved())
                    .commentCount(entity.getCommentCount())
                    .imageUrls(imageUrls)
                    .build();
        }).collect(Collectors.toList());
    }

    public Optional<BoardDTO> findById(Long id, String userId) {
        return boardRepository.findById(id).map(entity -> {
            String nickname = userService.getNickNameByUserId(entity.getUserId());
            List<String> imageUrls = parseImageUrls(entity.getImageUrl());
            boolean isLiked = userId != null && isLikedByUser(id, userId);
            long likeCount = getLikeCount(id);

            return BoardDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .userId(entity.getUserId())
                    .nickName(nickname)
                    .writingTime(entity.getWritingTime())
                    .category(entity.getCategory())
                    .likeCount(likeCount)
                    .isLiked(isLiked)
                    .isSaved(entity.isSaved())
                    .commentCount(entity.getCommentCount())
                    .imageUrls(imageUrls)
                    .build();
        });
    }


    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public BoardEntity update(Long id, BoardDTO dto) {
        BoardEntity entity = boardRepository.findById(id).orElseThrow();

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCategory(dto.getCategory());

        try {
            String imageUrlsJson = objectMapper.writeValueAsString(dto.getImageUrls());
            entity.setImageUrl(imageUrlsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return boardRepository.save(entity);
    }

    public BoardEntity toggleLike(Long boardId, String userId) {
        Optional<BoardLikeEntity> existing = likeRepository.findByBoardIdAndUserId(boardId, userId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
        } else {
            likeRepository.save(BoardLikeEntity.builder().boardId(boardId).userId(userId).build());
        }

        return boardRepository.findById(boardId).orElseThrow();
    }

    public boolean isLikedByUser(Long boardId, String userId) {
        return likeRepository.findByBoardIdAndUserId(boardId, userId).isPresent();
    }

    public long getLikeCount(Long boardId) {
        return likeRepository.countByBoardId(boardId);
    }

    public List<BoardDTO> findAll(String userId) {
        return boardRepository.findAll().stream().map(entity -> {
            List<String> imageUrls = parseImageUrls(entity.getImageUrl());

            return BoardDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .userId(entity.getUserId())
                    .nickName(userService.getNickNameByUserId(entity.getUserId()))
                    .writingTime(entity.getWritingTime())
                    .category(entity.getCategory())
                    .imageUrls(imageUrls)
                    .likeCount(getLikeCount(entity.getId()))
                    .isLiked(userId != null && isLikedByUser(entity.getId(), userId))
                    .commentCount(commentRepository.countByBoardId(entity.getId())) // ✅ 추가
                    .isSaved(entity.isSaved())
                    .build();
        }).collect(Collectors.toList());
    }


    public BoardEntity toggleSave(Long id) {
        BoardEntity entity = boardRepository.findById(id).orElseThrow();
        entity.setSaved(!entity.isSaved());
        return boardRepository.save(entity);
    }

    // ✅ JSON 문자열을 List<String>으로 파싱하는 헬퍼 메서드
    private List<String> parseImageUrls(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 실패 시 빈 리스트 반환
        }
    }
}
