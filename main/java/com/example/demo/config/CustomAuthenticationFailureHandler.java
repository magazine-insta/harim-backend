package com.example.demo.config;

import com.example.demo.config.exception.BaseException;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.config.exception.BaseResponseStatus.CANNOT_FIND_USERID;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    String errorMe = null;
    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
            errorMe = "아이디 또는 비밀번호를 찾을 수 없습니다";

            System.out.println("실패 custom");
            //throw new BaseException(CANNOT_FIND_USERID);
        }

        request.setAttribute("error", errorMe);
        response.sendRedirect("/api/loginf");
        //dispatcher.forward(request, response);
    }
}
