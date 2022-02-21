package com.example.demo.post.service;

import com.example.demo.config.exception.BaseException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.dto.PostRes;
import com.example.demo.post.dto.PostResSingle;
import com.example.demo.user.domain.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostRes uploadPost(MultipartFile contentsFile, String contents, String username);

    PostRes updatePost(Long id, PostReq postReq) throws Exception;

    PostRes updatePost2(Long id, MultipartFile multipartFile, String contents, String username) throws Exception;
    
    List<PostRes> getAllPosts(UserDetailsImpl username);

    PostResSingle getPost(String username, Long postId) throws BaseException;

    void delete(Long postId);

    void like(String username, Long postId);

    void uploadPost2(PostReq postReq, String username);
}
