package com.example.demo.post.service;

import com.example.demo.post.controller.AWSComponent;
import com.example.demo.post.domain.LikesRepository;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostRepository;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.dto.PostRes;
import com.example.demo.post.dto.PostResSingle;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostServiceImplTest {
//
//    @Autowired
//    private MockMvc mockMvc;

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
        //Mockito mock 을 사용한 단위 테스트

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

        // then
        assertEquals(content_Image, result.get().getContentImg());

    }

    @Test
    void getPost() throws Exception {
        //Mockito mock 을 사용한 단위 테스트

        // given
        Long postId = 100L;
        String username = "testUser";
        String content_Image = "postUpdate";

        PostServiceImpl productService = new PostServiceImpl(postRepository, userRepository, likesRepository, aws);
        when(userRepository.findByUserId(username))
                .thenReturn(Optional.of(new User(username)));
        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getPost(username,postId);
        });

        // then
        assertEquals(
                "포스트를 찾을 수 없습니다",
                exception.getMessage()
        );

    }

//    //통합 테스트 - 구현
    @Autowired
    PostServiceImpl postService;
//
//    Long userId = 100L;
//    Product createdProduct = null;
//    int updatedMyPrice = -1;
//
    @Test
    @DisplayName("게시글 등록")
    void test1() {
        // given
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        String content_Image = "postUpload";
        String username = "u1s7";

        PostReq requestPostDto = new PostReq(
                content_Image
        );


        // when
//        when(userRepository.findByUserId(username))
//                .thenReturn(Optional.of(new User(username)));

        //PostServiceImpl productService = new PostServiceImpl(postRepository, userRepository, likesRepository, aws);
        Post post = postService.uploadPost2(requestPostDto, username);

        // then

        assertNotNull(post.getId());
        assertEquals(username, post.getUser().getUserId());
        assertEquals(content_Image, post.getContentImg());

    }




}