package com.example.demo.user.service;

import com.example.demo.user.dto.SignupReq;

public interface UserService {
    void register(SignupReq signupReq) throws Exception;
}
