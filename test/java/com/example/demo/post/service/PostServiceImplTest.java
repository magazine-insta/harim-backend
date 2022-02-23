package com.example.demo.post.service;

import com.example.demo.post.controller.AWSComponent;
import com.example.demo.post.domain.LikesRepository;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostRepository;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.dto.PostRes;
import com.example.demo.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    LikesRepository likesRepository;
    @Mock
    AWSComponent aws;


    @Test
    void updatePost() throws Exception {
        // given
        Long postId = 100L;
        String username = "testUser";
        String content_Image = "postUpdate";

        PostReq requestPostDto = new PostReq(
                content_Image
        );

        Post post = new Post(
                content_Image
        );

        PostServiceImpl productService = new PostServiceImpl(postRepository, userRepository, likesRepository, aws);
        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));
        // when
        Optional<Post> result = productService.updatePost(postId,requestPostDto);
        System.out.println(result);
//
//        // then
        assertEquals(content_Image, result.get().getContentImg());


    }
}