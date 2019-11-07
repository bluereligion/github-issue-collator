package com.bluereligion.githubissuecollator.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a GitHub repository issue.
 */
public class Issue {

    public Issue() {}

    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    private String state;
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    private String repository;
    public String getRepository() { return repository; }
    public void setRepository(String repository) { this.repository = repository; }

    private String created_at;
    public String getCreated_at() {
        if ( Objects.isNull(created_at) || created_at.isEmpty() ) {
            return createdAt.toString();
        }
        else return created_at;
    }

    public void setCreated_at(String created_at) { this.created_at = created_at; }


    @JsonIgnore
    private LocalDateTime createdAt;
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        this.created_at = createdAt.toString();
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                ", repository='" + repository + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return id.equals(issue.id) &&
                Objects.equals(state, issue.state) &&
                Objects.equals(title, issue.title) &&
                repository.equals(issue.repository) &&
                Objects.equals(createdAt, issue.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, title, repository, createdAt);
    }
}
