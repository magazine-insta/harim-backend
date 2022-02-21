package com.example.demo.user.dto;

import lombok.Getter;

@Getter
public class SignupReq {
    private String user_id;

    private String nickname;

    private String password;
}
