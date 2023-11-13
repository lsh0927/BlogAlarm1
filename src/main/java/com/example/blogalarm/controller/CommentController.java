package com.example.blogalarm.controller;

import com.example.blogalarm.domain.Commit;
import com.example.blogalarm.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CommitController {

    private final CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("/commits")
    public String getAllCommits(Model model) {
        List<Commit> commits = commitService.getAllCommits();
        model.addAttribute("commits", commits);
        return "commitList";
    }

    @GetMapping("/commits/{id}")
    public String viewCommit(@PathVariable Long id, Model model) {
        Commit commit = commitService.getCommitById(id);
        model.addAttribute("commit", commit);
        return "commitView";
    }
}
