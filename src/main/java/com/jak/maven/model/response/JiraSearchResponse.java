package com.jak.maven.model.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"expand", "startAt", "maxResults", "total", "issues"})
public class JiraSearchResponse {

    @JsonProperty("expand")
    private String expand;
    @JsonProperty("startAt")
    private Integer startAt;
    @JsonProperty("maxResults")
    private Integer maxResults;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("issues")
    private List<Issue> issues = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("expand")
    public String getExpand() {
        return expand;
    }

    @JsonProperty("expand")
    public void setExpand(String expand) {
        this.expand = expand;
    }

    public JiraSearchResponse withExpand(String expand) {
        this.expand = expand;
        return this;
    }

    @JsonProperty("startAt")
    public Integer getStartAt() {
        return startAt;
    }

    @JsonProperty("startAt")
    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public JiraSearchResponse withStartAt(Integer startAt) {
        this.startAt = startAt;
        return this;
    }

    @JsonProperty("maxResults")
    public Integer getMaxResults() {
        return maxResults;
    }

    @JsonProperty("maxResults")
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public JiraSearchResponse withMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    public JiraSearchResponse withTotal(Integer total) {
        this.total = total;
        return this;
    }

    @JsonProperty("issues")
    public List<Issue> getIssues() {
        return issues;
    }

    @JsonProperty("issues")
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public JiraSearchResponse withIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public JiraSearchResponse withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JiraSearchResponse{");
        sb.append("expand='")
                .append(expand)
                .append('\'');
        sb.append(", startAt=")
                .append(startAt);
        sb.append(", maxResults=")
                .append(maxResults);
        sb.append(", total=")
                .append(total);
        sb.append(", issues=")
                .append(issues);
        sb.append(", additionalProperties=")
                .append(additionalProperties);
        sb.append('}');
        return sb.toString();
    }
}
