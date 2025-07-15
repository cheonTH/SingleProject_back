package com.single.map.model;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@AutoConfigurationPackage
@NoArgsConstructor
@Entity
@Builder
public class BoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;         // 게시글 제목
    private String content;       // 본문
    private String userId;        // 작성자 ID 또는 닉네임
    private String nickName;

    private String writingTime;

    private long likeCount = 0;
    
    @JsonProperty("isLiked")
    private boolean isLiked = false;
    
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    
    @Column
    private String category;
    
    private int commentCount = 0;
    
    @JsonProperty("isSaved")
    private boolean isSaved = false;	
}
