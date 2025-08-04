package com.tom.ociepa.github_repo_browser;

import com.tom.ociepa.github_repo_browser.model.BranchDto;
import com.tom.ociepa.github_repo_browser.model.RepositoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNonForkRepositoriesWithBranches() {
        // given
        String existingUsername = "octocat";

        // when
        String url = "http://localhost:" + port + "/users/" + existingUsername + "/repos";
        RepositoryResponse[] response = restTemplate.getForObject(url, RepositoryResponse[].class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.length).isGreaterThan(0);

        for (RepositoryResponse repo : response) {
            assertThat(repo.getRepositoryName()).isNotEmpty();
            assertThat(repo.getOwnerLogin()).isEqualToIgnoringCase(existingUsername);
            List<BranchDto> branches = repo.getBranches();
            assertThat(branches).isNotNull();
            for (BranchDto branch : branches) {
                assertThat(branch.getName()).isNotEmpty();
                assertThat(branch.getLastCommitSha()).isNotEmpty();
            }
        }
    }
}