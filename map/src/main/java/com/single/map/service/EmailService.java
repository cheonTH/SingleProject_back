package com.single.map.service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 인증번호를 이메일 기준으로 저장할 Map
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

    // 인증코드 생성
    public String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // 이메일 전송 및 코드 저장
    public String sendVerificationCode(String toEmail) {
        String code = generateCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);

        mailSender.send(message);

        // 인증 코드 저장 (간단한 메모리 기반 저장)
        verificationCodes.put(toEmail, code);
        
        System.out.println("이메일 전송: " + toEmail);

        return code;
    }

    // 인증번호 검증
    public boolean verifyCode(String email, String inputCode) {
        String savedCode = verificationCodes.get(email);
        return savedCode != null && savedCode.equals(inputCode);
    }
}
