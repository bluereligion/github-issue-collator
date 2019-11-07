package com.bluereligion.githubissuecollator.dto;

import java.util.List;

/**
 * Represents the TopDay information - the day and the occurences.
 */
public class TopDay {

    public TopDay() {}

    private String day;
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    private List<Occurrence> occurrences;
    public List<Occurrence> getOccurrences() { return occurrences; }
    public void setOccurrences(List<Occurrence> occurrences) { this.occurrences = occurrences; }

    @Override
    public String toString() {
        return "TopDay{" +
                "day='" + day + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }

}
