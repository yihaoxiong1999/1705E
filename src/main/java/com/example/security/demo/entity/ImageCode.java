package com.example.security.demo.entity;

import lombok.*;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageCode {

    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
