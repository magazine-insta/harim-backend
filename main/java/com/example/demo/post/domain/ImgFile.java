package com.example.demo.post.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImgFile {
    private String path;
    private String fileName;
    private String urlImg;
}
