package org.study.springbbs.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.study.springbbs.entity.Member;
import org.study.springbbs.model.UserInfo;
import org.study.springbbs.repository.UserRepository;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원 입니다."));
        return new UserInfo(member);
    }
}
