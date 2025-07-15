package com.single.map.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.single.map.dto.UserDTO;
import com.single.map.model.UserEntity;
import com.single.map.repository.UserRepository;
import com.single.map.security.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;

    public UserEntity create(UserDTO dto) {
        if (repository.findByUserId(dto.getUserId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        UserEntity user = UserEntity.builder()
                .name(dto.getName())
                .userId(dto.getUserId())
                .nickName(dto.getNickName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();

        return repository.save(user);
    }

    public UserDTO login(String userId, String password) {
        UserEntity user = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.create(user); // JWT 생성

        return UserDTO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .name(user.getName())
                .token(token)
                .build();
    }
    
    public String getNickNameByUserId(String userId) {
    	
    	System.out.println("요청된 userId: " + userId);
    	System.out.println("조회 결과: " + repository.findByUserId(userId));
        return repository.findByUserId(userId)
                .map(UserEntity::getNickName)
                .orElse("익명");
    }
    
    public String findUserIdByNameAndEmail(String name, String email) {
        return repository.findByNameAndEmail(name, email)
                .map(UserEntity::getUserId)
                .orElseThrow(() -> new RuntimeException("일치하는 사용자 정보를 찾을 수 없습니다."));
    }
    
    public void resetPasswordByUserIdAndEmail(String userId, String email, String newPassword) {
        UserEntity user = repository.findByUserIdAndEmail(userId, email)
            .orElseThrow(() -> new RuntimeException("일치하는 사용자를 찾을 수 없습니다."));

        // 새 비밀번호가 현재 비밀번호와 동일한지 확인
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("기존 비밀번호와 같은 비밀번호는 사용할 수 없습니다.");
        }

        // 새 비밀번호 암호화 및 저장
        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }

    public boolean checkCurrentPassword(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 로그인한 사용자 ID

        UserEntity user = repository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return passwordEncoder.matches(password, user.getPassword());
    }

    public void updateProfile(String nickname, String email, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 현재 로그인한 사용자 ID

        UserEntity user = repository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (nickname != null && !nickname.equals(user.getNickName())) {
            if (repository.existsByNickName(nickname)) {
                throw new RuntimeException("닉네임이 이미 존재합니다.");
            }
            user.setNickName(nickname);
        }

        if (email != null) user.setEmail(email);
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        repository.save(user);
    }


}
