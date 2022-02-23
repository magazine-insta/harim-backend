package com.example.demo.post.domain;

import com.example.demo.post.dto.PostReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    @Test
    @DisplayName("정상")
    void createPost_Normal() {
        // given
        Long userId = 100L;
        String title = "오리온 꼬북칩 초코츄러스맛 160g";
        String contents_image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
        String contents = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
        int lprice = 2350;

        PostReq requestDto = new PostReq(
                userId,
                title,
                contents,
                contents_image

        );

        // when
        Post product = new Post(requestDto);

        // then
        assertNull(product.getId());
        //assertEquals(userId, product.getUserId());
        assertEquals(title, product.getTitle());
        assertEquals(contents_image, product.getContentImg());
        assertEquals(contents, product.getContentText());

    }


}