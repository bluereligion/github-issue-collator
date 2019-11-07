package com.bluereligion.githubissuecollator.dto;

/**
 * Represents an Occurence - the num of issues for a repository.
 */
public class Occurrence {

    public Occurrence() {}

    private String repository;
    public String getRepository() { return repository; }
    public void setRepository(String repository) { this.repository = repository; }

    private int count;
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    @Override
    public String toString() {
        return "Occurrence{" +
                "repository='" + repository + '\'' +
                ", count=" + count +
                '}';
    }
}
