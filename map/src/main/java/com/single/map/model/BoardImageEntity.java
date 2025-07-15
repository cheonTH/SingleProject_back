package com.single.map.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 ID (연관관계 없이 Long 값만 저장)
    private Long boardId;

    // 이미지 URL
    private String imageUrl;
}
