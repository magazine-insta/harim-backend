package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter{

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:8081");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        filter.setFilterProcessesUrl("/api/login");
//
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.afterPropertiesSet();

        http.csrf()
               .ignoringAntMatchers("/api/**");
        http    //.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests(request->
                        request.antMatchers("/api/login/", "/api/loginf").permitAll()

                                .antMatchers("/api/register").permitAll()
                                .antMatchers(HttpMethod.GET,"/api/post").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin()
                    .loginProcessingUrl("/api/login")
                .failureHandler(failureHandler())
                .successHandler(successHandler())
                .and()
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout.logoutSuccessUrl("/"));



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

}
