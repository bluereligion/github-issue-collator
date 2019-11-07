package com.bluereligion.githubissuecollator;

/**
 * GitHub Issues -------------
 *
 * Create a program that generates a report about the the Issues belonging to a
 * list of github repositories ordered by creation time, and information about
 * the day when most Issues were created.
 *
 * Input: ----- List of 1 to n Strings with Github repositories references with
 * the format "owner/repository"
 *
 *
 * Output: ------ String representation of a Json dictionary with the following
 * content:
 *
 * - "issues": List containing all the Issues related to all the repositories
 * provided. The list should be ordered by the Issue "created_at" field (From
 * oldest to newest) Each entry of the list will be a dictionary with basic
 * Issue information: "id", "state", "title", "repository" and "created_at"
 * fields. Issue entry example: { "id": 1, "state": "open", "title": "Found a
 * bug", "repository": "owner1/repository1", "created_at":
 * "2011-04-22T13:33:48Z" }
 *
 * - "top_day": Dictionary with the information of the day when most Issues were
 * created. It will contain the day and the number of Issues that were created
 * on each repository this day If there are more than one "top_day", the latest
 * one should be used. example: { "day": "2011-04-22", "occurrences": {
 * "owner1/repository1": 8, "owner2/repository2": 0, "owner3/repository3": 2 } }
 *
 *
 * Output example: --------------
 *
 * {
 * "issues": [ { "id": 38, "state": "open", "title": "Found a bug",
 * "repository": "owner1/repository1", "created_at": "2011-04-22T13:33:48Z" }, {
 * "id": 23, "state": "open", "title": "Found a bug 2", "repository":
 * "owner1/repository1", "created_at": "2011-04-22T18:24:32Z" }, { "id": 24,
 * "state": "closed", "title": "Feature request", "repository":
 * "owner2/repository2", "created_at": "2011-05-08T09:15:20Z" } ], "top_day": {
 * "day": "2011-04-22", "occurrences": { "owner1/repository1": 2,
 * "owner2/repository2": 0 } } }
 *
 * --------------------------------------------------------
 *
 */

import com.bluereligion.githubissuecollator.dto.Response;
import com.bluereligion.githubissuecollator.service.GitHubIssuesService;
import com.bluereligion.githubissuecollator.util.IssueCollatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class IssueCollatorMain
        implements CommandLineRunner {

    @Autowired
    private GitHubIssuesService gitHubIssuesService;

    /**
     * @param args String array with Github repositories with the format
     * "owner/repository"
     *
     */
    public static void main(String[] args) {

        new SpringApplicationBuilder(IssueCollatorMain.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)
                .web(WebApplicationType.NONE)
                .properties("spring.config.name=application")
                .build()
                .run(args);
    }

    public void run(String... args) {

        if ( args.length == 0 ) {
            System.out.println("At least one repo needs to be added.");
            System.exit(1);
        }

        List<String> repos = Arrays.asList(args[0].split(","));
        System.out.println("Let's code!");
        Response response = null;

        try {
            response = this.gitHubIssuesService.getIssueStats(repos);
        }
        catch(Exception ex) {
            System.out.println("An exception occurred");
            ex.printStackTrace();
            System.exit(1);
        }

        if (Objects.isNull(response) ) {
            System.out.println("No response retrieved");
            this.printErrors();
            System.exit(1);
        }

        try {
            System.out.println(IssueCollatorUtils.convertToJson(response));
            this.printErrors();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.exit(0);

    }

    private void printErrors() {
        List<String> errors = this.gitHubIssuesService.getErrors();
        if ( Objects.isNull(errors) || errors.size() == 0 ) {
            System.out.println("No processing errors encountered");
        }
        else {
            System.out.println("Processing errors were encountered.\n");
            for ( String s : errors ) {
                System.out.println("Error="+s);
            }
        }
    }
}

