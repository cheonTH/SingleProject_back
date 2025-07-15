package com.single.map.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String userId;
    private String nickName;
    private String writingTime;
    private long likeCount;
    private boolean isLiked;
    private List<String> imageUrls;
    private String category;
    private int commentCount;
    private boolean isSaved;
}
