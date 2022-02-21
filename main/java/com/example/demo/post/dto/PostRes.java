package com.example.demo.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostRes {
    private Long post_id;
    private String nickname;
    private String use_image;
    private LocalDateTime created_at;
    private String contents_text;
    private String contents_image;
    private boolean like;
}
