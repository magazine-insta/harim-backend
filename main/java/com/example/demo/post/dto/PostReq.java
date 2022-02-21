package com.example.demo.post.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostReq {
    private String user_id;

    private String title;

    private String contents;

    private String contents_image;

    //private MultipartFile contentsImage;
}
