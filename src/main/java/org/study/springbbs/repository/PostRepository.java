package org.study.springbbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.springbbs.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    public List<Post> findPostsByUseYn(String useYn);
}
