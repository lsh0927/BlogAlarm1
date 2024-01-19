package com.example.blogalarm.controller;


import com.example.blogalarm.domain.Member;
import com.example.blogalarm.domain.Post;
import com.example.blogalarm.form.PostForm;
import com.example.blogalarm.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "posts/postList";

    }


    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/postView";
    }


    @GetMapping("/posts/create")
    public String createPostForm(Model model, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");

        if (loggedInMember != null) {
            // 현재 로그인된 회원 정보를 모델에 추가
            model.addAttribute("loggedInMember", loggedInMember);
            model.addAttribute("postForm", new PostForm());
            return "posts/PostCreateForm";
        } else {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트 또는 처리
            return "redirect:/login";
        }
    }

    // 게시물 생성 요청 처리
    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute PostForm postForm, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");


        if (loggedInMember != null) {
            // 현재 로그인된 회원의 ID를 사용하여 포스팅 생성
            Post post = new Post();
            post.setMember(loggedInMember);

            post.setTitle(postForm.getTitle());
            post.setContent(postForm.getContent());
            post.setId(loggedInMember.getId());

            postService.savePost(post);
            return "redirect:/posts";
        } else {
            // 로그인되지 않은 경우 로그인 페이지로 다시
            return "redirect:/login";
        }
    }

    @GetMapping("/posts/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        if (post == null) {
            // 게시물이 존재하지 않으면 오류 처리
            System.out.println("Post가 없어요");
            return "error"; // 오류 처리 템플릿 또는 페이지로 리다이렉트
        }
        PostForm postForm = new PostForm();
        postForm.setId(post.getId()); // 수정: postId 대신 id 사용
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        model.addAttribute("postForm", postForm);
        return "posts/editPostForm";
    }

    // 게시물 수정 요청 처리
    @PostMapping("/posts/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute PostForm postForm) {
        Post post = postService.getPostById(id);
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        postService.updatePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");

        if (loggedInMember == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트 또는 처리
            return "redirect:/login";
        }
        Post post = postService.getPostById(id);

        // post가 null인 경우 처리
        if (post == null) {
            System.out.println("게시물이 존재하지 않습니다.");
            return "error";
        }

        // 현재 로그인된 회원이 작성한 게시물인지 확인
        if (!loggedInMember.getId().equals(post.getMemberId())) {
            System.out.println("회원 정보 불일치");
            return "error";
        }

        // 회원 정보가 일치하면 게시물 삭제
        postService.deletePost(id);

        return "redirect:/posts";
    }


}
