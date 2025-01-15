package org.study.springbbs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.study.springbbs.entity.Member;
import org.study.springbbs.entity.Post;
import org.study.springbbs.model.PostRequest;
import org.study.springbbs.model.PostResponse;
import org.study.springbbs.model.UserInfo;
import org.study.springbbs.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponse> getAllList(){
        return postRepository.findPostsByUseYn("Y").stream().map(post->{
            return PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .writer(post.getMember().getUsername())
                    .createAt(post.getCreatedAt())
                    .updateAt(post.getUpdatedAt()).build();
        }).toList();
    }

    public PostResponse getPost(Integer id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(()-> new Exception("해당되는 post정보가 없습니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getMember().getUsername())
                .createAt(post.getCreatedAt())
                .updateAt(post.getUpdatedAt()).build();
    }

    public Post savePost(PostRequest request, Member member){
        return postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .useYn("Y")
                .build());
    }

    public Post modPost(PostRequest request, Member member) throws Exception {
        Post post = postRepository.findById(request.getId()).orElseThrow(()-> new Exception("해당되는 post정보가 없습니다."));
        if( !post.getMember().getId().equals( member.getId() ) ){
            throw new Exception("본인이 작성한 post만 수정할 수있습니다.");
        }
        post.setTitle(request.getTitle());
        post.setContent( request.getContent());
        return postRepository.save(post);
    }

    public PostResponse deletePost(Integer id, Member member) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(()-> new Exception("해당되는 post정보가 없습니다."));
        if( !post.getMember().getId().equals( member.getId() ) ){
            throw new Exception("본인이 작성한 post만 수정할 수있습니다.");
        }
        post.setUseYn("N");
        postRepository.save(post);
        return PostResponse.builder()
                .id(post.getId())
                .updateAt(post.getUpdatedAt()).build();
    }
}
