package com.single.map.model;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class UserEntity {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	private String name;
	@Column(nullable = false, unique = true)
	private String userId;
	@Column(nullable = false, unique = true)
	private String nickName;
	
	private String email;
	private String password;
	
}
