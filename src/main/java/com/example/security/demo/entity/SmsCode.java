package com.example.security.demo.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class SmsCode {
    private String code;
    private LocalDateTime expireTime;


    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
