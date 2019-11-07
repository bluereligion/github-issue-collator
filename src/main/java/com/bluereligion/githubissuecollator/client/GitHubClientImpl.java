package com.bluereligion.githubissuecollator.client;

import com.bluereligion.githubissuecollator.dto.Issue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Iterator;

import com.bluereligion.githubissuecollator.util.IssueCollatorUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the request/response to the GitHub API
 */
@Component("gitHubIssuesClient")
public class GitHubClientImpl
        implements GitHubClient {

    @Value("${application.serviceUrl}")
    private String serviceUrl;  // https://api.github.com/repos

    private ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate = new RestTemplate();
    private static String GET_ISSUES_BY_REPOSITORY_PATH = "/issues?state=all&sort=created&direction=asc";
    private static Logger LOGGER = LoggerFactory.getLogger(GitHubClientImpl.class);

    // https://api.github.com/repos/vmg/redcarpet/issues?state=all&sort=created&direction=asc

    /**
     * Makes request to the GitHub issue services for the request owner/repository
     * @param ownerRepository
     * @return
     * @throws HttpServerErrorException
     * @throws IllegalArgumentException
     * @throws Exception
     */
    @Override
    public List<Issue> getIssuesByRepository(String ownerRepository) throws HttpServerErrorException, IllegalArgumentException, Exception {
        LOGGER.debug("ownerRepository="+ownerRepository);

        if ( Objects.isNull(ownerRepository) )
            throw new IllegalArgumentException("ownerRepository parameter passed to getIssuesByRepository is either null or has a null id.");

        String uri = formulateUrl(ownerRepository);
        LOGGER.debug("uri="+uri);

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(uri, JsonNode.class);
        LOGGER.debug("Response="+response);

        if ( Objects.isNull(response) ||  Objects.isNull(response.getBody()) ) {
            String msg = String.format("No body from API request received for ownerRepository=%s", ownerRepository);
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if ( !response.getStatusCode().is2xxSuccessful() ) {
            throw new HttpServerErrorException(response.getStatusCode(), ( response.getBody() != null ) ? response.getBody().toString() : "");
        }

        return this.processResponse(response.getBody(), ownerRepository);

    }

    /**
     * Extracts the desired fields from the GitHub issue response.
     * @param body
     * @param ownerRepository
     * @return
     */
    protected List<Issue> processResponse(JsonNode body, String ownerRepository) {
        List<Issue> issues = new ArrayList<>();
        ArrayNode issuesNode = (ArrayNode)body;
        Iterator<JsonNode> iter = issuesNode.elements();

        while ( iter.hasNext() ) {
            JsonNode jsonIssue = iter.next();
            Issue issue = new Issue();
            issue.setRepository(ownerRepository);

            if ( jsonIssue.hasNonNull("id") )
                issue.setId(jsonIssue.get("id").asLong());
            if ( jsonIssue.hasNonNull("state") )
                issue.setState(jsonIssue.get("state").asText());
            if ( jsonIssue.hasNonNull("title") )
                issue.setTitle(jsonIssue.get("title").asText());
            if ( jsonIssue.hasNonNull("created_at") )
                issue.setCreatedAt(IssueCollatorUtils.convertToLocalDateTime((jsonIssue.get("created_at").asText())));

            issues.add(issue);
        }
        return issues;
    }


    protected String formulateUrl(String ownerRepository) {
        LOGGER.debug("serviceUrl="+serviceUrl);
        LOGGER.debug("ownerRepository="+ownerRepository);
        LOGGER.debug("GET_ISSUES_BY_REPOSITORY_PATH="+GET_ISSUES_BY_REPOSITORY_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append(serviceUrl).append("/").append(ownerRepository).append(GET_ISSUES_BY_REPOSITORY_PATH);
        return sb.toString();
    }

    protected void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
