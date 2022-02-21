package com.example.demo.config;

import com.example.demo.user.dto.LoginReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    public CustomLoginFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }
    private ObjectMapper objectMapper = new ObjectMapper();

    AuthenticationProvider authenticationProvider;
    DaoAuthenticationProvider daoAuthenticationProvider;
    //AuthenticationProvider authenticationProvider;
    SecurityContextPersistenceFilter securityContextPersistenceFilter;
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        LoginReq loginReq = objectMapper.readValue(messageBody, LoginReq.class);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginReq.getUser_id(), loginReq.getPassword());
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        System.out.println(this.getAuthenticationManager());
        return this.getAuthenticationManager().authenticate(authRequest);
//        }
    }
}
