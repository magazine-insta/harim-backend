package com.example.demo.user.controller;

import com.example.demo.config.exception.BaseException;
import com.example.demo.user.dto.SignupReq;
import com.example.demo.user.service.UserService;
import com.example.demo.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(value = "*" ,allowedHeaders = "*") // 이후 삭제예정
public class UserController {
    private final UserService userService;

    @PostMapping("/register") // ResponseEntity
    public void register(@RequestBody SignupReq registerRequestUser) throws Exception {

        userService.register(registerRequestUser);

    }

}
