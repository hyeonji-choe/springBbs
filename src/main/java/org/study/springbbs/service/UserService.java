package org.study.springbbs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.springbbs.entity.Member;
import org.study.springbbs.model.MemberJoinRequest;
import org.study.springbbs.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String join(MemberJoinRequest request) throws Exception {
        if(userRepository.findByUsername( request.getUsername() ).isPresent()) {
            throw new Exception("이미 존재하는 회원입니다.");
        }
        Member newMem = userRepository.save(request.toEntity(passwordEncoder));
        return newMem.getUsername();
    }
}
