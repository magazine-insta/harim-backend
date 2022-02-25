package com.example.demo.user.controller;

import com.example.demo.config.CustomLoginFilter;
import com.example.demo.config.VerifyResult;
import com.example.demo.config.WebConfig;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.dto.PostReq;
import com.example.demo.post.service.PostService;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserDetailsImpl;
import com.example.demo.user.domain.UserRoleEnum;
import com.example.demo.user.dto.SignupReq;
import com.example.demo.user.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(
//        controllers = {UserController.class, PostController.class},
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = WebConfig.class
//                )
//        }
//)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test")).andExpect(status().isForbidden());

        String username = "nonexistentuser";
        String password = "password";

//
        mvc.perform(MockMvcRequestBuilders.get("/api/post/15")
                        )
                .andExpect(status().isForbidden()).andReturn();
    }


    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule());

    public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
            String username = "u1s7";
            String password = "uspss";

            String body = "{\"user_id\":\"" + username + "\", \"password\":\""
                    + password + "\"}";

            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/login")
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();



            VerifyResult verifyResult = parseResponse(result, VerifyResult.class);


            mvc.perform(MockMvcRequestBuilders.get("/api/post/15")
                            .header("Authorization", "Bearer " + verifyResult.getAccess()))
                    .andExpect(status().isOk());
    }


    // MVC test , spring security

   private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;
//
    @Autowired
    private ObjectMapper objectMapper;
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    PostService postService;
//
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "u1s6";
        String password = "uspss";
        String email = "hope@sparta.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }


    @Test
    @DisplayName("회원 가입 요청 처리")
    void register() throws Exception {
        String username = "제이홉";
        String password = "hope!@#";

        SignupReq requestDto = new SignupReq(
                username, password
        );

        String postInfo = objectMapper.writeValueAsString(requestDto);
        System.out.println(postInfo);
        // when - then
        mvc.perform(post("/api/register")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        //.accept(MediaType.APPLICATION_JSON)
                        //.principal(mockPrincipal)
                )
                //.andExpect(status().is3xxRedirection())
                //.andExpect(view().name("redirect:/user/login"))
                //.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("쿠키 세션 로그인 확인")
    void test3() throws Exception {
        // given
        this.mockUserSetup();
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        PostReq requestDto = new PostReq(
                title
        );


        // when - then
        mvc.perform(MockMvcRequestBuilders.get("/api/post/15")
                       )
                .andExpect(status().isOk());


        //안됨...실패...
    }

}