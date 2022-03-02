package com.example.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.config.exception.BaseException;
import com.example.demo.user.domain.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
public class JWTUtil {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("hangehe");
    private static final long AUTH_TIME = 20*60;
    private static final long REFRESH_TIME = 60*60*24*7;

    public String getUsername(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = bearer.substring("Bearer ".length());

        VerifyResult result = JWTUtil.verify(token);
        return result.getUsername();
    }

//    @Value("spring.jwt.secret")
//    private String secretKey;
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }


    //private final UserDetailsService userDetailsService;


//    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    // Jwt Token에서 User PK 추출
//    public String getUserPk(String token) {
//        return JWT.parser().setSigningKey(secretKey)
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public String resolveToken(HttpServletRequest req) {
//        return req.getHeader("X-AUTH-TOKEN");
//    }


    public static String makeAuthToken(UserDetailsImpl user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(UserDetailsImpl user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder().success(true)
                    .username(verify.getSubject()).build();
        }catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().success(false)
                    .username(decode.getSubject()).build();
        }
    }
}
