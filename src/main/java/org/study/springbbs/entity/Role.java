package org.study.springbbs.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER","일반사용자");
    private final String key;
    private final String title;
}
