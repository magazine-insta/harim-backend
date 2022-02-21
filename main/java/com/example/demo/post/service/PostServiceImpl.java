package com.example.demo.post.service;

import com.example.demo.config.exception.BaseException;
import com.example.demo.post.controller.AWSComponent;
import com.example.demo.post.domain.*;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.dto.PostRes;
import com.example.demo.post.dto.PostResSingle;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserDetailsImpl;
import com.example.demo.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.config.exception.BaseResponseStatus.CANNOT_FIND_POSTID;
import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    private final AWSComponent fileStore;

//
//    @Override
//    public byte[] downloadTodoImage(Long id) {
//        return fileStore.download(todo.getImagePath(), todo.getImageFileName());
//    }

    private ImgFile uploadImg(MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3
        String path = String.format("%s/%s", bucket, UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        String url = "";
        try {
             url = fileStore.uploadStorage(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return ImgFile.builder().path(path).fileName(fileName).urlImg(url).build();
//        쓰기 요청 분산
//        FileEvent.toCompleteEvent(dt);
//
//        FileEvent.toErrorEvent(d);

    }



    @Override
    public PostRes uploadPost(MultipartFile contentsFile, String contents, String username) {
        ImgFile s3Url = uploadImg(contentsFile); // MultipartFile

        Optional<User> user = userRepository.findByUserId(username);
        Post post = Post.builder()
                .user(user.get())
                .createdAt(LocalDateTime.now())
                .contentText(contents)
                .contentImg(s3Url.getUrlImg()).build();
        postRepository.save(post);
        return null;
    }

    @Override
    public void uploadPost2(PostReq postReq, String username) {
        Optional<User> user = userRepository.findByUserId(username);
        Post post = Post.builder()
                .user(user.get())
                .createdAt(LocalDateTime.now())
                .contentText(postReq.getContents())
                .contentImg(postReq.getContents_image()).build();
        postRepository.save(post);
    }

    @Override
    public PostRes updatePost(Long id, PostReq postReq) throws Exception {

        Optional<Post> post = postRepository.findById(id);
//        if (postReq.getUser_id() != post.get().getUser().getUserId()){
//            throw new Exception("접근 할 수 없다");
//        }
        if (postReq.getContents() != null){
        post.get()
                .setContentText(postReq.getContents());
            }
        if(postReq.getContents_image() != null){
            //String s3Url = uploadImg(postReq.getContents_image()).toString(); // MultipartFile
            post.get().setContentImg(postReq.getContents_image());
        }

        return null;
    }

    @Override
    public PostRes updatePost2(java.lang.Long id, MultipartFile multipartFile, String contents, String username) throws Exception {
        Optional<User> user = userRepository.findByUserId(username);

        Optional<Post> post = postRepository.findById(id);
        if (user.get().getId() != post.get().getUser().getId()){
            throw new Exception("접근 할 수 없다");
        }
        if (contents != null){
            post.get()
                    .setContentText(contents);
        }
        if(multipartFile != null){
            ImgFile s3Url = uploadImg(multipartFile); // MultipartFile
            post.get().setContentImg(s3Url.getUrlImg());
        }

        return null;
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostRes> getAllPosts(UserDetailsImpl username) {
        List<Post> posts = (List<Post>) postRepository.findAll();
        if (username != null){
            Long userId = userRepository.findByUserId(username.getUsername())
                    .get().getId();

            return posts.stream()
                    .map(post -> {
                        Optional<Likes> like = likesRepository.findIdByUserAndPost(userId, post.getId());

                        return PostRes.builder()
                                .post_id(post.getId())
                                .nickname(post.getUser().getNickname())
                                .use_image(post.getUser().getUserImg())
                                .created_at(post.getCreatedAt())
                                .contents_text(post.getContentText())
                                .contents_image(post.getContentImg())
                                .like((like.isEmpty()) ? false : true)
                                .build();
                    })
                    .collect(Collectors.toList());
        } else {
            return posts.stream()
                    .map(post -> {


                        return PostRes.builder()
                                .post_id(post.getId())
                                .nickname(post.getUser().getNickname())
                                .use_image(post.getUser().getUserImg())
                                .created_at(post.getCreatedAt())
                                .contents_text(post.getContentText())
                                .contents_image(post.getContentImg())
                                .like(false)
                                .build();
                    })
                    .collect(Collectors.toList());
        }
    }


    @Override
    @Transactional(readOnly=true)
    public PostResSingle getPost(String username, Long postId) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        Long userId = userRepository.findByUserId(username)
                .get().getId();
        Optional<Likes> like = likesRepository.findIdByUserAndPost(userId, postId);

        if (post == null){
            throw new BaseException(CANNOT_FIND_POSTID);
        }

        return PostResSingle.builder()
                .post_id(postId)
                .nickname(post.get().getUser().getNickname())
                //.use_image(post.get().getUser().getUserImg())
                .profile_image(post.get().getUser().getUserImg())
                .created_at(post.get().getCreatedAt())
                .contents_text(post.get().getContentText())
                .contents_image(post.get().getContentImg())
                .like((like.isEmpty()) ? false : true)
                .build();
    }

    @Override
    public void delete(java.lang.Long postId) {
        //post 사용자, 로그인 사용자 같은 지 확인해야

        postRepository.deleteById(postId);
    }

    @Override
    public void like(String username, java.lang.Long postId) {
        Optional<User> user = userRepository.findByUserId(username);

        Optional<Post> post = postRepository.findById(postId);

        Optional<Likes> like = likesRepository.findIdByUserAndPost(user.get().getId(), postId);

        if (like.isEmpty()){
            likesRepository.save(Likes.builder().user(user.get().getId()).post(post.get().getId()).build());
        } else {
            likesRepository.deleteById(like.get().getId());
        }

    }

}
