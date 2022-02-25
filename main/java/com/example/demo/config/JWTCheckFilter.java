package com.example.demo.config;

import com.example.demo.user.domain.UserDetailsImpl;
import com.example.demo.user.service.UserDetailsServiceImpl;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        VerifyResult result = JWTUtil.verify(token);
        if(result.isSuccess()){
            UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(result.getUsername());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            //setDetails(request, authRequest);
            //userToken.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response);

        }else{
            request.setAttribute("exception",  "Token is not valid");
            //request.setAttribute("errorCode", CefLoadHandler.ErrorCode.ERR_INVALID_AUTH_CREDENTIALS.getCode());
            throw new AuthenticationException("Token is not valid");
        }
    }
}
