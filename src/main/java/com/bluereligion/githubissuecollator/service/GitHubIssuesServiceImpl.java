package com.bluereligion.githubissuecollator.service;

import java.time.LocalDate;
import java.util.*;

import com.bluereligion.githubissuecollator.client.GitHubClient;
import com.bluereligion.githubissuecollator.dto.Issue;
import com.bluereligion.githubissuecollator.dto.Occurrence;
import com.bluereligion.githubissuecollator.dto.Response;
import com.bluereligion.githubissuecollator.dto.TopDay;
import com.bluereligion.githubissuecollator.util.RepositoryIssueMap;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service that takes a set of repos.  ALLOWS for duplicate repos.
 * This service retrieves the issues from the GitHub API and aggregates them and
 * returns them along with Top Day stats.
 */
@Component("gitHubIssuesService")
public class GitHubIssuesServiceImpl
    implements GitHubIssuesService {

    @Autowired
    RepositoryIssueMap repositoryIssueMap;

    @Autowired
    GitHubClient gitHubIssuesClient;

    private List<String> errors = new ArrayList<>();
    private static Logger LOGGER = LoggerFactory.getLogger(GitHubIssuesServiceImpl.class);


    /**
     * Processes the repositories. Accumulates the issues for each and returns the top issue day and
     * all of the occurrence stats for that day.
     *
     * @param repositoriesTEST
     * @return
     */
    @Override
    public Response getIssueStats(List<String> repositories) throws IllegalArgumentException {
        //String[] repositories = {"shadowsocks/shadowsocks-android", "octocat/Hello-World", "vmg/redcarpet", "iptv-org/iptv"};

        if ( Objects.isNull(repositories) || repositories.size() < 1 ) {
            LOGGER.error("repositories param is null or empty.");
            throw new IllegalArgumentException("repositories param is null or empty.");
        }

        for ( int index = 0; index < repositories.size(); index++ ) {
            String repo = repositories.get(index);
            if ( isRepoValid(repo) ) {
                LOGGER.debug("Procesing issues for repo="+repo);
                this.repositoryIssueMap.processIssues(this.getIssues(repo));
            }
            else {
                LOGGER.debug("Invalid Repo="+repo);
                errors.add("Invalid Repo="+repo);
            }
        }
        List<Issue> aggregatedIssues = this.repositoryIssueMap.getSortedAggregatedList();
        if ( Objects.isNull(aggregatedIssues) ) {
            LOGGER.error("aggregatedIssues is null");
        }
        else {
            LOGGER.debug("aggregatedIssues.size()="+aggregatedIssues.size());
        }

        Response response = new Response();
        response.setIssues(aggregatedIssues);
        response.setTopDay(this.getTopDayForIssues());
        return response;
    }

    /**
     * Performs a validaity check on the repo argument enter by the users.
     * @param repository
     * @return
     */
    private boolean isRepoValid(String repository) {
        if ( Objects.isNull(repository) || repository.isEmpty() || !repository.contains("/") ) return false;
        else return true;
    }


    /**
     *  Retreives the issues from the GitHub issue API for a given repo
     *
     * @param repository
     * @return
     */
    private List<Issue> getIssues(String repository) {
        List<Issue> issues = new ArrayList<>();
        try {
            issues = gitHubIssuesClient.getIssuesByRepository(repository);
        }
        catch(Exception ex) {
            LOGGER.error("Error getting issues from GitHub for the following repository="+repository, ex);
            errors.add("TODO repo"+" Failed to get issues from GitHub.");
        }
        return issues;
    }


    /**
     * Returns the Top Day and occurrence stats for this run.
     * @return
     */
    private TopDay getTopDayForIssues() throws IllegalStateException {
        LocalDate topDayDate = this.repositoryIssueMap.getDateWithMostIssues();
        if ( Objects.isNull(topDayDate) ) {
            LOGGER.error("No Top of Day info found");
            throw new IllegalStateException("No Top of Day info found");
        }

        LOGGER.debug("Top Date="+topDayDate.toString());

        List<Occurrence> occurrences = this.repositoryIssueMap.getOccurrencesforDay(topDayDate);
        LOGGER.debug("Total top day occurrences="+occurrences.size());
        TopDay topDay = new TopDay();
        topDay.setDay(topDayDate.toString());
        topDay.setOccurrences(occurrences);

        return topDay;
    }


    /**
     * Returns a list of any errors encountered.
     * @return
     */
    @Override
    public List<String> getErrors() {
        this.errors.addAll(this.repositoryIssueMap.getErrors());
        return this.errors;
    }
}
