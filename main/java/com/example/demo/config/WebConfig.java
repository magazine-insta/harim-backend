package com.example.demo.config;

import com.example.demo.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter{





//
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080", "http://localhost:8081");
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), userService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService);

        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        filter.setFilterProcessesUrl("/api/login");

        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.afterPropertiesSet();

        http.cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin().disable()
//                .failureHandler(failureHandler())
//                .and()

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {

                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException authException) throws IOException, ServletException {
                        String exception = (String)request.getAttribute("exception");
                        //String errorCode = (String)request.getAttribute("errorCode");

                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setStatus(403);

                        if (exception == null){
                            response.getWriter().println("{ \"message\" : \"" + "로그인 하세요"
                                    + "\", \"code\" : \"" + "required login"
                                    + "\", \"status\" : " + 404
                                    + ", \"errors\" : [ ] }");
                        }else{

                        response.getWriter().println("{ \"message\" : \"" + exception
                                + "\", \"code\" : \"" + "expired token"
                                + "\", \"status\" : " + 404
                                + ", \"errors\" : [ ] }");
                        }
                    }});






        http.csrf()
               .ignoringAntMatchers("/api/**");
        http    //.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests(request->
                        request.antMatchers("/api/login/", "/api/loginf", "/errorjwt").permitAll()

                                .antMatchers("/api/signup").permitAll()
                                .antMatchers(HttpMethod.GET,"/api/post").permitAll()
                                .anyRequest().authenticated()
                );
//                .formLogin()
//                    .loginProcessingUrl("/api/login")
//                .failureHandler(failureHandler())
//                .successHandler(successHandler())
//                .and()
//                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logout->logout.logoutSuccessUrl("/"));



//
//        http.csrf()
//                .ignoringAntMatchers("/api/**");
//        http.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class);
//        http
//                .authorizeRequests()
//                .antMatchers("/api/register").permitAll()
//                .antMatchers("/api/login").permitAll()
//                .antMatchers(HttpMethod.GET,"/api/post").permitAll()
//
//                .anyRequest().authenticated()
//                .and()
//                // 로그인 기능 허용
//                    //http.formLogin()
//                    .loginProcessingUrl("/api/login")
//                    .defaultSuccessUrl("/")
//                    .failureHandler(failureHandler())
//                    .failureUrl("/user/login?error")
//
//                    .permitAll()
//
//                .and()
//                    // 로그아웃 기능 허용
//                    .logout()
//                    .permitAll();
////                .sessionManagement(session->
////                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                )
////                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
////                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
//        ;

    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }






    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("*");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
