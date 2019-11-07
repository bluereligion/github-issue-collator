package com.bluereligion.githubissuecollator.util;

import com.bluereligion.githubissuecollator.dto.Issue;
import com.bluereligion.githubissuecollator.dto.Occurrence;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.*;
import org.mockito.Mockito;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RepositoryIssueMapTest {

    @InjectMocks
    RepositoryIssueMap repositoryIssueMap = new RepositoryIssueMap();

    @Test
    public void testMapPropagation() throws Exception {
        List<Issue> issues = new ArrayList<>();

        Issue one = new Issue();
        one.setId(737025l);
        one.setState("closed");
        one.setTitle("autolink should include hash/anchor part of URLs");
        one.setRepository("vmg/redcarpet");
        String date = "2019-04-06T08:58:12Z";
        Instant instant = Instant.parse(date);
        one.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(one);

        Issue two = new Issue();
        two.setId(737025l);
        two.setState("closed");
        two.setTitle("autolink should include hash/anchor part of URLs");
        two.setRepository("vmg/redcarpet");
        date = "2019-04-06T09:08:12Z";
        instant = Instant.parse(date);
        two.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(two);

        Issue three = new Issue();
        three.setId(737025l);
        three.setState("closed");
        three.setTitle("autolink should include hash/anchor part of URLs");
        three.setRepository("octocat/Hello-World");
        date = "2019-03-06T19:58:12Z";
        instant = Instant.parse(date);
        three.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(three);

        Issue four = new Issue();
        four.setId(737025l);
        four.setState("closed");
        four.setTitle("autolink should include hash/anchor part of URLs");
        four.setRepository("godotengine/godot");
        date = "2019-11-06T08:58:12Z";
        instant = Instant.parse(date);
        four.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(four);

        this.repositoryIssueMap.processIssues(issues);
        List<Issue> aggIssues = this.repositoryIssueMap.getSortedAggregatedList();
        Map<LocalDate, List<Occurrence>> map = this.repositoryIssueMap.getDateOccurrenceMap();

        System.out.println("aggIssues.size()="+aggIssues.size());
        System.out.println("map.size() == 3="+map.size());

        assertTrue(aggIssues.size() == 4);
        assertTrue(map.size() == 3);
    }


    @Test
    public void testAggregationOrdering() throws Exception {
        List<Issue> issues = new ArrayList<>();

        Issue one = new Issue();
        one.setId(737025l);
        one.setState("closed");
        one.setTitle("autolink should include hash/anchor part of URLs");
        one.setRepository("vmg/redcarpet");
        String date = "2018-04-06T08:58:12Z";
        Instant instant = Instant.parse(date);
        one.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(one);

        Issue two = new Issue();
        two.setId(737025l);
        two.setState("closed");
        two.setTitle("autolink should include hash/anchor part of URLs");
        two.setRepository("vmg/redcarpet");
        date = "2019-04-06T09:08:12Z";
        instant = Instant.parse(date);
        two.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(two);

        Issue three = new Issue();
        three.setId(737025l);
        three.setState("closed");
        three.setTitle("autolink should include hash/anchor part of URLs");
        three.setRepository("octocat/Hello-World");
        date = "2019-03-06T19:58:12Z";
        instant = Instant.parse(date);
        three.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(three);

        Issue four = new Issue();
        four.setId(737025l);
        four.setState("closed");
        four.setTitle("autolink should include hash/anchor part of URLs");
        four.setRepository("godotengine/godot");
        date = "2019-11-06T08:58:12Z";
        instant = Instant.parse(date);
        four.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(four);

        this.repositoryIssueMap.processIssues(issues);
        List<Issue> aggIssues = this.repositoryIssueMap.getSortedAggregatedList();

        assertTrue(aggIssues.get(0).equals(one));

    }

    @Test
    public void testHighestIssueDateWithOneHighestDate() throws Exception {
        List<Issue> issues = new ArrayList<>();

        Issue one = new Issue();
        one.setId(737025l);
        one.setState("closed");
        one.setTitle("autolink should include hash/anchor part of URLs");
        one.setRepository("vmg/redcarpet");
        String date = "2019-04-06T08:58:12Z";
        Instant instant = Instant.parse(date);
        one.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(one);

        Issue two = new Issue();
        two.setId(737025l);
        two.setState("closed");
        two.setTitle("autolink should include hash/anchor part of URLs");
        two.setRepository("vmg/redcarpet");
        date = "2019-04-06T09:08:12Z";
        instant = Instant.parse(date);
        two.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(two);

        Issue three = new Issue();
        three.setId(737025l);
        three.setState("closed");
        three.setTitle("autolink should include hash/anchor part of URLs");
        three.setRepository("octocat/Hello-World");
        date = "2019-03-06T19:58:12Z";
        instant = Instant.parse(date);
        three.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(three);

        Issue four = new Issue();
        four.setId(737025l);
        four.setState("closed");
        four.setTitle("autolink should include hash/anchor part of URLs");
        four.setRepository("godotengine/godot");
        date = "2019-11-06T08:58:12Z";
        instant = Instant.parse(date);
        four.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(four);

        //2019-04-06T00:00
        LocalDate expected = LocalDate.of(2019, Month.APRIL, 06);
        this.repositoryIssueMap.processIssues(issues);
        LocalDate most = this.repositoryIssueMap.getDateWithMostIssues();
        assertTrue(most.equals(expected));
    }


    @Test
    public void testHighestIssueDateWithTwoHighestDate() throws Exception {
        List<Issue> issues = new ArrayList<>();

        Issue one = new Issue();
        one.setId(737025l);
        one.setState("closed");
        one.setTitle("autolink should include hash/anchor part of URLs");
        one.setRepository("vmg/redcarpet");
        String date = "2019-04-06T08:58:12Z";
        Instant instant = Instant.parse(date);
        one.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(one);

        Issue two = new Issue();
        two.setId(737025l);
        two.setState("closed");
        two.setTitle("autolink should include hash/anchor part of URLs");
        two.setRepository("vmg/redcarpet");
        date = "2019-04-06T09:08:12Z";
        instant = Instant.parse(date);
        two.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(two);

        Issue three = new Issue();
        three.setId(737025l);
        three.setState("closed");
        three.setTitle("autolink should include hash/anchor part of URLs");
        three.setRepository("octocat/Hello-World");
        date = "2019-05-06T19:58:12Z";
        instant = Instant.parse(date);
        three.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(three);

        Issue four = new Issue();
        four.setId(737025l);
        four.setState("closed");
        four.setTitle("autolink should include hash/anchor part of URLs");
        four.setRepository("godotengine/godot");
        date = "2019-05-06T08:58:12Z";
        instant = Instant.parse(date);
        four.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(four);

        //2019-05-06T00:00
        LocalDate expected = LocalDate.of(2019, Month.MAY, 06);
        this.repositoryIssueMap.processIssues(issues);
        LocalDate most = this.repositoryIssueMap.getDateWithMostIssues();
        assertTrue(most.equals(expected));
    }

    @Test
    public void testOccurrencesByDay() throws Exception {
        List<Issue> issues = new ArrayList<>();

        Issue one = new Issue();
        one.setId(737025l);
        one.setState("closed");
        one.setTitle("autolink should include hash/anchor part of URLs");
        one.setRepository("vmg/redcarpet");
        String date = "2019-04-06T08:58:12Z";
        Instant instant = Instant.parse(date);
        one.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(one);

        Issue two = new Issue();
        two.setId(737025l);
        two.setState("closed");
        two.setTitle("autolink should include hash/anchor part of URLs");
        two.setRepository("vmg/redcarpet");
        date = "2019-04-06T09:08:12Z";
        instant = Instant.parse(date);
        two.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(two);

        Issue three = new Issue();
        three.setId(737025l);
        three.setState("closed");
        three.setTitle("autolink should include hash/anchor part of URLs");
        three.setRepository("octocat/Hello-World");
        date = "2019-04-06T19:58:12Z";
        instant = Instant.parse(date);
        three.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(three);

        Issue four = new Issue();
        four.setId(737025l);
        four.setState("closed");
        four.setTitle("autolink should include hash/anchor part of URLs");
        four.setRepository("godotengine/godot");
        date = "2019-04-06T08:58:12Z";
        instant = Instant.parse(date);
        four.setCreatedAt(LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())));
        issues.add(four);

        this.repositoryIssueMap.processIssues(issues);
        LocalDate most = this.repositoryIssueMap.getDateWithMostIssues();
        List<Occurrence> occurrences =this.repositoryIssueMap.getOccurrencesforDay(most);

        Occurrence o = occurrences.get(2);

        assertTrue(occurrences.size() == 3);
        assertTrue(o.getCount() == 2);
    }

}
