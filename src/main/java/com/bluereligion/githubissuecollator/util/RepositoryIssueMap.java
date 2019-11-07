package com.bluereligion.githubissuecollator.util;

import com.bluereligion.githubissuecollator.dto.Issue;
import com.bluereligion.githubissuecollator.dto.Occurrence;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("repositoryIssueMap")
public class RepositoryIssueMap {

    private Map<LocalDate, List<Occurrence>> dateOccurrenceMap = new HashMap<>();
    private List<Issue> aggregatedIssues = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private Set<String> repos = new HashSet<>();

    private static Logger LOGGER = LoggerFactory.getLogger(RepositoryIssueMap.class);

    /**
     * Iterates through the issues and stores info by repo and day.
     * @param issues
     */
    public void processIssues(List<Issue> issues) {
        if (Objects.isNull(issues) || issues.size() == 0 ) {
            LOGGER.debug("Param issues is null or empty.");
            return;
        }
        this.addIssuesToDateOccurenceMap(issues);
        this.aggregatedIssues.addAll(issues);
    }

    /**
     * Manages the issue counts by day.
     * @param issues
     */
    private void addIssuesToDateOccurenceMap(List<Issue> issues) {
        for ( Issue issue : issues ) {
            LOGGER.debug("Adding Issue="+issue);

            if ( Objects.isNull(issue.getCreatedAt()) ) {
                errors.add("Created Date was null, discarding issue="+issue);
            }
            else {
                String repo = issue.getRepository();
                LocalDate createdLocalDateTime = issue.getCreatedAt().toLocalDate();
                Occurrence occurrence = new Occurrence();
                occurrence.setCount(1);
                occurrence.setRepository(repo);
                this.repos.add(repo);

                if (dateOccurrenceMap.containsKey(createdLocalDateTime)) {
                    dateOccurrenceMap.put(createdLocalDateTime, dateOccurrenceMap.get(createdLocalDateTime)).add(occurrence);
                } else {
                    List<Occurrence> list = new ArrayList<>();
                    list.add(occurrence);
                    dateOccurrenceMap.put(createdLocalDateTime, list);
                }
            }
        }

    }

    /**
     * Returns the day with the most issues.
     * @return day with the most issues.
     */
    public LocalDate getDateWithMostIssues() {
        LocalDate mostIssuesDate = LocalDate.of(1900, Month.JANUARY, 01);
        int mostIssueAmount = 0;

        for ( Map.Entry<LocalDate, List<Occurrence>> entry : dateOccurrenceMap.entrySet() ) {
            if ( entry.getValue().size() > mostIssueAmount ) {
                mostIssuesDate = entry.getKey();
                mostIssueAmount = entry.getValue().size();
            }
            if ( entry.getValue().size() == mostIssueAmount ) {
                if ( mostIssuesDate.isBefore(entry.getKey()) )
                mostIssuesDate = entry.getKey();
            }
        }
        return mostIssuesDate;
    }

    /**
     * Returns the aggregated list of all repo/issues.
     * @return aggregated issues
     */
    public List<Issue> getSortedAggregatedList() {
        return this.aggregatedIssues.stream().sorted(Comparator.comparing(Issue::getCreatedAt)).collect(Collectors.toList());
    }

    /**
     * Returns the occurrences for a given day.
     * @param day
     * @return the occurrences for the day.
     */
    public List<Occurrence> getOccurrencesforDay(LocalDate day) throws IllegalStateException {
        if ( day == null ) {
            LOGGER.debug("Day param is null");
            return null;
        }

        List<Occurrence> occurrences = null;

        try {
            occurrences = (dateOccurrenceMap.get(day)).stream().sorted(Comparator.comparing(Occurrence::getRepository)).collect(Collectors.toList());
        }
        catch(Exception ex) {
            throw new IllegalStateException("No Top of Day or Occurrence information was found");
        }
        List<Occurrence> response = new ArrayList<>();

        Occurrence current = occurrences.get(0);
        Occurrence occurrence = new Occurrence();
        String currentRepo = current.getRepository();

        int numOfOccurrences = 0;
        int index = 0;

        while ( index < occurrences.size() ) {
            while ( current.getRepository().equals(currentRepo) ) {
                this.repos.remove(currentRepo);
                numOfOccurrences++;
                index++;
                if ( index >= occurrences.size() ) break;
                current = occurrences.get(index);
            }

            occurrence.setRepository(currentRepo);
            occurrence.setCount(numOfOccurrences);
            response.add(occurrence);
            occurrence = new Occurrence();
            numOfOccurrences = 0;
            currentRepo = current.getRepository();
        }

        // catch repos with zero occurrences for that day
        if ( this.repos.size() > 0 ) {
            for ( String s : this.repos ) {
                occurrence = new Occurrence();
                occurrence.setRepository(s);
                occurrence.setCount(0);
                response.add(occurrence);

            }
        }
        return response;
    }

    public Map<LocalDate, List<Occurrence>> getDateOccurrenceMap() {
        return dateOccurrenceMap;
    }

    public List<String> getErrors() { return this.errors; }

}
