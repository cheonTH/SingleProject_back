package com.single.map.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.single.map.dto.UserDTO;
import com.single.map.model.UserEntity;
import com.single.map.repository.UserRepository;
import com.single.map.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService service;
	private final UserRepository userRepository;
	
	@GetMapping("/check-userId")
	public ResponseEntity<?> checkUserId(@RequestParam String userId) {
	    boolean exists = userRepository.existsByUserId(userId);
	    return ResponseEntity.ok(Map.of("available", !exists));
	}

	@GetMapping("/check-nickname")
	public ResponseEntity<?> checkNickname(@RequestParam String nickName) {
	    boolean exists = userRepository.existsByNickName(nickName);
	    return ResponseEntity.ok(Map.of("available", !exists));
	}
	
	@PostMapping("/signup")
    public ResponseEntity<?> Signup(@RequestBody UserDTO dto) {
        try {
            UserEntity user = service.create(dto);
            UserDTO response = UserDTO.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .name(user.getName())
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> Login(@RequestBody UserDTO dto) {
	    try {
	        // 로그인 처리 및 토큰 생성
	        UserDTO response = service.login(dto.getUserId(), dto.getPassword());

	        return ResponseEntity.ok().body(response);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
	    }
	}
	
	@GetMapping("/find-userId")
	public ResponseEntity<?> findUserIds(@RequestParam String name, @RequestParam String email) {
	    List<String> userIds = userRepository.findAllByNameAndEmail(name, email)
	            .stream()
	            .map(UserEntity::getUserId)
	            .toList();

	    if (userIds.isEmpty()) {
	        return ResponseEntity.badRequest().body("일치하는 아이디가 없습니다.");
	    }

	    return ResponseEntity.ok(Map.of("userIds", userIds));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
	    String userId = payload.get("userId");
	    String email = payload.get("email");
	    String newPassword = payload.get("newPassword");

	    try {
	        service.resetPasswordByUserIdAndEmail(userId, email, newPassword);
	        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("재설정 실패: " + e.getMessage());
	    }
	}

	@PostMapping("/check-password")
	public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> request) {
	    String password = request.get("password");
	    try {
	        boolean isValid = service.checkCurrentPassword(password);
	        return ResponseEntity.ok(Map.of("success", isValid));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("비밀번호 확인 실패: " + e.getMessage());
	    }
	}

	@PutMapping("/update-profile")
	public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request) {
	    String nickname = request.get("nickname");
	    String email = request.get("email");
	    String password = request.get("password"); // 새 비밀번호

	    try {
	        service.updateProfile(nickname, email, password);
	        return ResponseEntity.ok("수정 완료");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("수정 실패: " + e.getMessage());
	    }
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> getMyInfo(Authentication authentication) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(403).body("인증되지 않은 사용자입니다.");
	    }

	    String userId = authentication.getName(); // JWT 기반 인증이면 여기서 userId(email 등) 나옴
	    UserEntity user = userRepository.findByUserId(userId)
	        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

	    return ResponseEntity.ok(Map.of(
	        "userId", user.getUserId(),
	        "name", user.getName(),
	        "email", user.getEmail(),
	        "nickName", user.getNickName()
	    ));
	}


}
