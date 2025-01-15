package org.study.springbbs.model;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.study.springbbs.entity.Member;
import org.study.springbbs.entity.Role;

@NoArgsConstructor
@Getter
@Setter
public class MemberJoinRequest {
    private String username;
    private String password;

    @Builder
    public MemberJoinRequest(String username,String password){
        this.username = username;
        this.password = password;
    }

    public Member toEntity(PasswordEncoder passwordEncoder){
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER).build();
    }
}
