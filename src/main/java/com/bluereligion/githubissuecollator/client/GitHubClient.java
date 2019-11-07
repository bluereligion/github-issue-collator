package com.bluereligion.githubissuecollator.client;

import java.util.List;
import com.bluereligion.githubissuecollator.dto.Issue;

/**
 * Encapsulates the request/response to the GitHub API
 */
public interface GitHubClient {

    /**
     * Makes request to the GitHub issue services for the request owner/repository
     * @param ownerRepository
     * @return
     * @throws HttpServerErrorException
     * @throws IllegalArgumentException
     * @throws Exception
     */
    List<Issue> getIssuesByRepository(String ownerRepository) throws Exception;

}
