package org.study.springbbs.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.study.springbbs.entity.Member;
import org.study.springbbs.model.MemberJoinRequest;
import org.study.springbbs.service.CustomUserDetailService;
import org.study.springbbs.service.UserService;
import org.study.springbbs.util.JwtUtil;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailsService;
    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> join(MemberJoinRequest request) throws Exception {
        return ResponseEntity.ok(userService.join(request));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginSuccessResponse> login( LoginRequest request, HttpServletResponse response) throws Exception {
        try {
            log.info("loginRequest.username = {}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            log.info("principal.username = {}", principal.getUsername());
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인 실패");
        }
        // 인증 성공 후 인증된 user의 정보를 갖고옴
        log.info("/auth username = {}", request.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        // subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
        String token = jwtUtil.generateToken(userDetails);
        // 생성된 토큰을 헤더에 세팅하여 클라이언트에게 응답
        response.addHeader("Authorization", "Bearer " + token);

        // 생성된 토큰을 응답 (Test)
        return ResponseEntity.ok(new LoginSuccessResponse(token));
    }
    @AllArgsConstructor
    @Data
    static class LoginRequest{
        private String username;
        private String password;
    }

    @AllArgsConstructor
    @Data
    static class LoginSuccessResponse {
        private String token;
    }
}
