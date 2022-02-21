package com.example.demo.config.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    CANNOT_FIND_POSTID(false, 2021, "포스트를 찾을 수 없습니다"),
    LOGINCHECK(false, 2020,"로그인하세요"),

    CANNOT_FIND_USERID(false, 2022, "아이디 찾을 수 없습니다"),
    CANNOT_FIND_PASSWORD(false, 2023, "비밀번호 틀렸습니다");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
