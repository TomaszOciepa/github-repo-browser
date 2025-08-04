package com.tom.ociepa.github_repo_browser.service;


import com.tom.ociepa.github_repo_browser.exception.UserNotFoundException;
import com.tom.ociepa.github_repo_browser.model.BranchDto;
import com.tom.ociepa.github_repo_browser.model.RepositoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GithubService {

    private static final String GITHUB_API_BASE = "https://api.github.com";

    private final RestTemplate restTemplate;

    public GithubService() {
        this.restTemplate = new RestTemplate();
    }

    public List<RepositoryResponse> getUserRepositoriesFromGithub(String username) {
        try {

            List<Map<String, Object>> repos = getAllRepositories(username);

            List<RepositoryResponse> result = new ArrayList<>();

            for (Map<String, Object> repo : repos) {
                Boolean fork = (Boolean) repo.get("fork");
                if (fork != null && fork) {
                    continue;
                }

                String repoName = (String) repo.get("name");
                Map<String, Object> ownerMap = (Map<String, Object>) repo.get("owner");
                String ownerLogin = (String) ownerMap.get("login");

                List<Map<String, Object>> branches = getBranches(ownerLogin, repoName);

                List<BranchDto> branchDtosList = new ArrayList<>();

                if (branches != null) {
                    for (Map<String, Object> branch : branches) {
                        String branchName = (String) branch.get("name");
                        Map<String, Object> commit = (Map<String, Object>) branch.get("commit");
                        String sha = commit != null ? (String) commit.get("sha") : null;

                        branchDtosList.add(new BranchDto(branchName, sha));
                    }
                }

                RepositoryResponse response = new RepositoryResponse(repoName, ownerLogin, branchDtosList);
                result.add(response);
            }

            return result;

        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException("User not found");
        }
    }

    private List<Map<String, Object>> getBranches(String ownerLogin, String repoName) {
        List<Map<String, Object>> branches = restTemplate.getForObject(
                GITHUB_API_BASE + "/repos/{owner}/{repo}/branches",
                List.class,
                ownerLogin,
                repoName
        );
        return branches;
    }

    private List<Map<String, Object>> getAllRepositories(String username) {
        List<Map<String, Object>> repos = restTemplate.getForObject(
                GITHUB_API_BASE + "/users/{username}/repos",
                List.class,
                username
        );
        return repos;
    }
}
