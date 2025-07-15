package com.single.map.controller;

import com.single.map.dto.BoardDTO;
import com.single.map.model.BoardEntity;
import com.single.map.service.BoardService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // ✅ JSON 기반 요청으로 변경 (이미지 제외)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody BoardDTO dto) {
        BoardEntity saved = boardService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader(value = "userId", required = false) String userId) {
        return ResponseEntity.ok(boardService.findAll(userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id,
                                    @RequestHeader(value = "userId", required = false) String userId) {
        return boardService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BoardDTO dto) {
        return ResponseEntity.ok(boardService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, @RequestHeader("userId") String userId) {
        boardService.toggleLike(id, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", boardService.isLikedByUser(id, userId));
        result.put("likeCount", boardService.getLikeCount(id));
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{id}/save")
    public ResponseEntity<?> toggleSave(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.toggleSave(id));
    }
}
