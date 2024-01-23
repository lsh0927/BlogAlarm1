package com.example.blogalarm.controller;

import com.example.blogalarm.domain.Comment;
import com.example.blogalarm.domain.Member;
import com.example.blogalarm.domain.Post;
import com.example.blogalarm.form.CommentForm;
import com.example.blogalarm.jwt.MemberToken;
import com.example.blogalarm.jwt.MemberTokenRepository;
import com.example.blogalarm.jwt.MemberTokenService;
import com.example.blogalarm.repository.MemberRepository;
import com.example.blogalarm.service.CommentService;
import com.example.blogalarm.service.CommentServiceImpl;
import com.example.blogalarm.service.MemberService;
import com.example.blogalarm.service.PostService;
import com.example.blogalarm.social.service.KakaoService;
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
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentServiceImpl commentService;
    private final KakaoService kakaoService;
    private final MemberTokenRepository memberTokenRepository;
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

//    @GetMapping("/comments/create")
//    public String createCommentForm(Model model) {
//        model.addAttribute("commentForm", new CommentForm());
//        return "comments/createCommentForm";
//    }

    //현재 createCommentForm에서, 댓글을 다는 유저가 포스트를 입력해 찾아야하는 로직 수정
    @GetMapping("/comments/create")
    public String createCommentForm(Model model) {
        List<Post> posts = postService.getAllPosts(); // 모든 포스트를 가져옴
        model.addAttribute("posts", posts); // 모델에 포스트 목록 추가
        model.addAttribute("commentForm", new CommentForm());
        return "comments/createCommentForm";
    }


    @PostMapping("/comments/create")
    // TODO: session에서 로그인한 멤버 ID를 가져오기

    public String createComment(@ModelAttribute CommentForm commentForm,  HttpSession session ) {
        Comment comment = new Comment();
        comment.setText(commentForm.getText());
        Member member = (Member) session.getAttribute("loggedInMember");
        System.out.println("Member : " +member.getNickname());




       // Member member= memberRepository.findById(Long.valueOf(id)).orElseThrow(null);
        // TODO: Optional의 사용법과 쓰는 이유
        comment.setMember(member);
        // Comment와 Post를 연결
//        Post post = postService.getPostById(commentForm.getPostId());

        Long postId = commentForm.getPostId(); // 폼에서 제공된 postId

        if (postId == null || postService.getPostById(postId) == null) {
            // 포스트가 유효하지 않은 경우, 오류 메시지와 함께 폼으로 다시 리다이렉트
            session.setAttribute("errorMessage", "유효하지 않은 포스트 입니다. 다시 선택하세요.");
            return "redirect:/comments/create";
        }


        //포스트의 주인
        Long SendUserId= postId;
        Post post = postService.getPostById(postId);


        String message = "새로운 댓글이 달렸습니다! " +
                "포스트: " + post.getTitle() +
                ", 작성자: " + comment.getMember().getNickname()+
                ", 내용: " + commentForm.getText();
//
        Optional<MemberToken> memberToken=memberTokenRepository.findByMemberId(SendUserId);
        String accessToken= memberToken.get().getAccessToken();
        System.out.println(accessToken);

        kakaoService.sendKakaoMessage(accessToken,message);


//        Post post = postService.getPostById(postId);


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
