package com.bluereligion.githubissuecollator.service;

import java.util.List;

import com.bluereligion.githubissuecollator.dto.Response;

/**
 * The service that takes a set of repos.  ALLOWS for duplicate repos.
 * This service retrieves the issues from the GitHub API and aggregates them and
 * returns them along with Top Day stats.
 */
public interface GitHubIssuesService {

    Response getIssueStats(List<String> repositories);

    List<String> getErrors();
}
