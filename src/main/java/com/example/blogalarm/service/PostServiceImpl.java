package com.example.blogalarm.service;


import com.example.blogalarm.domain.Member;
import com.example.blogalarm.domain.Post;
import com.example.blogalarm.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, MemberService memberService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    //Id로 Post 찾기
    @Override
    public Post getPostById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }


        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // 디버깅 코드
            System.out.println("Post ID: " + post.getId());
            System.out.println("Post Title: " + post.getTitle());
            System.out.println("Post Member ID: " + post.getMemberId());

            return post;
        } else {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
    }

    public List<Post> getAllPostsWithComments() {
        return postRepository.findAllWithComments();
    }
}