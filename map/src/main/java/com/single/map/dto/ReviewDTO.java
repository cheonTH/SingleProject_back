package com.single.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private String placeId;
    private String placeName;
    private String review;
    private String userId;
    private String nickName; // ✅ 닉네임 추가
}
