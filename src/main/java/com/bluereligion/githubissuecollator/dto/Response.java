package com.bluereligion.githubissuecollator.dto;

import java.util.List;

/**
 * This is the response object that is returned to the user.
 */
public class Response {

    public Response() {}

    private List<Issue> issues;
    public List<Issue> getIssues() { return issues; }
    public void setIssues(List<Issue> issues) { this.issues = issues; }

    private TopDay topDay;
    public TopDay getTopDay() { return topDay; }
    public void setTopDay(TopDay topDay) { this.topDay = topDay; }

    @Override
    public String toString() {
        return "Response{" +
                "issues=" + issues +
                ", topDay=" + topDay +
                '}';
    }

}
