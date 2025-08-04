package com.tom.ociepa.github_repo_browser.controller;

import com.tom.ociepa.github_repo_browser.exception.UserNotFoundException;
import com.tom.ociepa.github_repo_browser.model.ErrorResponse;
import com.tom.ociepa.github_repo_browser.model.RepositoryResponse;
import com.tom.ociepa.github_repo_browser.service.GithubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class RepositoryController {

    private final GithubService githubService;

    public RepositoryController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}/repos")
    public List<RepositoryResponse> getUserRepositories(@PathVariable String username) {
        return githubService.getRepositoriesWithBranches(username);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

