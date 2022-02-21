package com.example.demo.post.controller;



import com.example.demo.config.exception.BaseException;
import com.example.demo.post.dto.DeleteRes;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.dto.PostRes;
import com.example.demo.post.dto.PostResSingle;
import com.example.demo.post.service.PostService;
import com.example.demo.user.domain.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Security;
import java.util.List;

import static com.example.demo.config.exception.BaseResponseStatus.LOGINCHECK;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*" ,allowedHeaders = "*") // 이후 삭제예정
public class PostController {
    AnonymousAuthenticationFilter anonymousAuthenticationFilter;
    private final PostService postService;

    @GetMapping("/api/post")
    public List<PostRes> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return postService.getAllPosts(userDetails);
    }

    @GetMapping("/api/post/{postId}")
    public PostResSingle getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        if (userDetails == null){
//            throw new Exception("로그인 필요");
//        }

        try {
            return postService.getPost(userDetails.getUsername(), postId);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @PostMapping(value = "/api/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public void uploadPost(@RequestPart MultipartFile contentsImage,@RequestParam("contents") String contents, @AuthenticationPrincipal UserDetails userDetails) throws BaseException {
//
//        postService.uploadPost(contentsImage, contents, userDetails.getUsername());
//
//    }

    @PostMapping("/api/post")
    public void uploadPost(@RequestBody PostReq postReq, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        postService.uploadPost2(postReq, userDetails.getUsername());
    }

//    @PutMapping(value = "/api/post/:postId", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public void updatePost2(@PathVariable Long postId, @RequestPart MultipartFile contentsImage,@RequestParam("contents") String contents, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
//        postService.updatePost2(postId, contentsImage, contents, userDetails.getUsername());
//    }

    @DeleteMapping("/api/post/{postId}")
    public DeleteRes deletePost(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        if (userDetails == null){
            throw new Exception("로그인 필요");
        }
        postService.delete(postId);
        return DeleteRes.builder().post_id(postId).build();
    }

    @PutMapping("/api/post/{postId}")
    public void updatePost(@PathVariable Long postId, @RequestBody PostReq postReq) throws Exception {
        postService.updatePost(postId, postReq);
    }

    @GetMapping("/api/loginf")
    public String failogin(){
        return "아이디 또는 패스워드를 찾을 수 없습니다";
    }

    @GetMapping("/")
    public String successLogin(){
        return "성공";
    }

    @SneakyThrows
    @PutMapping("/api/post/{postId}/like")
    public void likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null){
            throw new BaseException(LOGINCHECK);
        }
        postService.like(userDetails.getUsername(), postId);

    }

}