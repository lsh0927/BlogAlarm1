package com.example.blogalarm.service;

import com.example.blogalarm.domain.Commit;
import com.example.blogalarm.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {

    private final CommitRepository commitRepository;

    @Autowired
    public CommitServiceImpl(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @Override
    public List<Commit> getCommitsForPost(Long postId) {
        // Implement the logic to get comments for a specific post
        return commitRepository.findAllByPostId(postId);
    }

    @Override
    public List<Commit> getAllCommits() {
        return commitRepository.findAll();
    }

    @Override
    public Commit getCommitById(Long id) {
        return commitRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCommit(Commit comment) {
        commitRepository.save(comment);
    }

    @Override
    public void updateCommit(Commit comment) {
        commitRepository.save(comment);
    }

    @Override
    public void deleteCommit(Long id) {
        commitRepository.deleteById(id);
    }
}
