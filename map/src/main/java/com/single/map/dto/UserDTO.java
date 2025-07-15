package com.single.map.dto;

import java.util.List;

import com.single.map.model.UserEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private String id;
	private String name;
	private String userId;
	private String nickName;
	private String email;
	private String password;
	private String token;
}
