package org.study.springbbs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.study.springbbs.entity.Post;
import org.study.springbbs.model.PostRequest;
import org.study.springbbs.model.PostResponse;
import org.study.springbbs.model.UserInfo;
import org.study.springbbs.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /*
    게시글작성
     */
    @GetMapping("/post/regist")
    public String postRegist(){
        return "/post/post_ins";
    }
    @PostMapping("/api/post")
    public ResponseEntity<Post> post (PostRequest request, @AuthenticationPrincipal UserInfo user){
        return ResponseEntity.ok(postService.savePost(request,user.getMember()));

    }

    /*
    게시글리스트
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> posts(){
        return ResponseEntity.ok(postService.getAllList());
    }

    /*
    게시글 상세조회
     */
    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(postService.getPost(id));
    }
    /*
    게시글 수정
     */
    @PutMapping("/api/post")
    public ResponseEntity<Post> modPost(PostRequest request, @AuthenticationPrincipal UserInfo user) throws Exception {
        return ResponseEntity.ok().body(
                postService.modPost(request,user.getMember()));
    }
    /*
    게시글 삭제
     */
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Integer id, @AuthenticationPrincipal UserInfo user) throws Exception {
        return ResponseEntity.ok(postService.deletePost(id,user.getMember()));
    }
}
