package com.example.blogalarm.service;


import com.example.blogalarm.domain.Post;
import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    void savePost(Post post);
    void updatePost(Post post);
    void deletePost(Long id);

    List<Post> getAllPostsWithComments();
}
