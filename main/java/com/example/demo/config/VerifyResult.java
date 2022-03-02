package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyResult {

    private boolean success;
    private String username;
    // 추가할수도
    private String token;
    private String refresh;

}
