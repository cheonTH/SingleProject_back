package com.single.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private Long boardId;
    private String userId;
    private String content;
    private Long parentId;
    private String createdTime;
    private String updatedTime;
    private String nickName;
}
