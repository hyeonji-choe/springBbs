package org.study.springbbs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.study.springbbs.entity.Role;
import org.study.springbbs.service.CustomUserDetailService;

import java.io.PrintWriter;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
public class SecurityConfig{

    private final CustomUserDetailService customUserDetailService;
    private final JwtAuthenticateFilter jwtAuthenticateFilter;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig)->csrfConfig.disable())//Cross site Request forgery 설정을 disable
                .authorizeHttpRequests((request)->
                                //메인화면과 로그인 및 회원가입 권한에 상관없이 접근
                        request.requestMatchers("/", "/api/auth/**","/posts","/member/**").permitAll()
                                //posts 관리 요청은 로그인 인증을 하여 USER 권한을 획득한 사용자만 접근
                                //.requestMatchers("/api/post/**","/post/**").hasRole(Role.USER.name())
                                .anyRequest().authenticated())
                //401,403 exception
                .exceptionHandling((exceptionConfig)->
                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler))
//                .formLogin((formLogin)->
//                        formLogin.disable())
//                        formLogin//.loginPage("/member/login")
//                                .usernameParameter("username")
//                                .passwordParameter("password")
//                                .loginProcessingUrl("/api/auth/login")//로그인 submit 요청을 받을 url
//                                .defaultSuccessUrl("/",true))//로그인성공시 이동할 url
                .logout((logoutConfig)->
                        logoutConfig.logoutSuccessUrl("/"))
                //submit을 통해 설정한 "/api/auth/login" 요청을 받으면 Spring Security는 요청을 받아 서비스 로직을 수행
                //.userDetailsService(customUserDetailService)
                //세션사용x
                .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                // UsernamePasswordAuthenticationFilter 에 도달하기 전에 커스텀한 필터를 먼저 동작
                .addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Spring security forbidden...");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {

        private final HttpStatus status;
        private final String message;
    }
}
