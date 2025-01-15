package org.study.springbbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.springbbs.entity.Member;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findByUsername(String username);
}
