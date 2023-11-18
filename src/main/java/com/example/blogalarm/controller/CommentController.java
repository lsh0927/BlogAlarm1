package com.example.blogalarm.controller;

import com.example.blogalarm.domain.Comment;
import com.example.blogalarm.domain.Member;
import com.example.blogalarm.domain.Post;
import com.example.blogalarm.form.CommentForm;
import com.example.blogalarm.repository.MemberRepository;
import com.example.blogalarm.service.CommentService;
import com.example.blogalarm.service.MemberService;
import com.example.blogalarm.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/comments")
    public String getAllComments(Model model) {
        List<Comment> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "comments/CommentList";
    }

    @GetMapping("/comments/{id}")
    public String viewComment(@PathVariable Long id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "comments/CommentList"; // 댓글 상세 정보를 보여줄 템플릿 이름
    }

    @GetMapping("/comments/create")
    public String createCommentForm(Model model) {
        model.addAttribute("commentForm", new CommentForm());
        return "comments/createCommentForm";
    }

    @PostMapping("/comments/create")
    // TODO: session에서 로그인한 멤버 ID를 가져오기

    public String createComment(@ModelAttribute CommentForm commentForm, Principal principal, HttpSession session ) {
        Comment comment = new Comment();
        comment.setText(commentForm.getText());
        Member member = (Member) session.getAttribute("loggedInMember");


        System.out.println("Member : " +member);

       // Member member= memberRepository.findById(Long.valueOf(id)).orElseThrow(null);
        // TODO: Optional의 사용법과 쓰는 이유

        comment.setMember(member);

        // Comment와 Post를 연결
        Post post = postService.getPostById(commentForm.getPostId());
        comment.setPost(post);

        commentService.saveComment(comment);
        return "redirect:/comments";
    }

    @GetMapping("/comments/edit/{id}")
    public String editCommentForm(@PathVariable Long id, Model model) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return "error";
        }
        CommentForm commentForm = new CommentForm();
        commentForm.setCommentId(id);
        commentForm.setText(comment.getText());
        model.addAttribute("commentForm", commentForm);
        return "comments/editCommentForm";
    }

    @PostMapping("/comments/edit/{id}")
    public String editComment(@PathVariable Long id, @ModelAttribute CommentForm commentForm) {
        Comment comment = commentService.getCommentById(id);
        comment.setText(commentForm.getText());
        commentService.updateComment(comment);
        return "redirect:/comments";
    }

    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "redirect:/comments";
    }
}
