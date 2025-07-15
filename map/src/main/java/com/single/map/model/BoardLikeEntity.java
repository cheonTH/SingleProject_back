package com.single.map.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_like", uniqueConstraints = @UniqueConstraint(columnNames = {"boardId", "userId"}))
public class BoardLikeEntity {
    @Id 
    @GeneratedValue
    private Long id;

    private Long boardId;
    private String userId; // 또는 user PK

    public BoardLikeEntity(Long boardId, String userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}