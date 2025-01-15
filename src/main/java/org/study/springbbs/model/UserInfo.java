package org.study.springbbs.model;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.study.springbbs.entity.Member;

import java.util.List;

@Getter
public class UserInfo extends User {
    private Member member;

    public UserInfo(Member member){
        super(member.getUsername(),member.getPassword(),List.of(new SimpleGrantedAuthority(member.getRole().name())));
        this.member = member;
    }
}
