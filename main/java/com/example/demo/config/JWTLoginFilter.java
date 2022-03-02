package com.example.demo.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.user.domain.UserDetailsImpl;
import com.example.demo.user.dto.LoginReq;
import com.example.demo.user.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserDetailsServiceImpl userService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userService) {
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/api/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {
        LoginReq userLogin = objectMapper.readValue(request.getInputStream(), LoginReq.class);
        if(userLogin.getRefreshToken() == null) {
            UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(userLogin.getUsername());


            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user, userLogin.getPassword(), null
            );
            // user details...
            return getAuthenticationManager().authenticate(token);
        }else{
            VerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            if(verify.isSuccess()){
                System.out.println(verify.getUsername());
                String username = verify.getUsername();

                UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(
                        user, user.getAuthorities()
                );
            }else{
                request.setAttribute("exception",  "refresh token expired");
                throw new TokenExpiredException("refresh token expired");
            }
        }


    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();
        String access =  JWTUtil.makeAuthToken(user);
        String refresh  = JWTUtil.makeRefreshToken(user);
        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(user));
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+JWTUtil.makeAuthToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        //response.getOutputStream().write(objectMapper.registerModule(new JavaTimeModule()).writeValueAsBytes(user));
        VerifyResult verifyResult = VerifyResult.builder().success(true).username(user.getUsername()).token(access).refresh(refresh).build();
        response.getOutputStream().write(objectMapper.writeValueAsBytes(verifyResult));
    }
}
