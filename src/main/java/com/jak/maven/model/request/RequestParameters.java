package com.jak.maven.model.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Immutable
public abstract class RequestParameters {
    private ObjectMapper objectMapper = new ObjectMapper();

    public abstract String jiraUrl();

    public abstract String user();

    public abstract String password();

    public abstract String jiraProject();

    public abstract String release();

    public abstract List<String> issueTypes();

    public abstract List<String> statuses();

    public abstract List<String> fields();

    public String buildJql() throws JsonProcessingException {

        Map<String, Object> map = new HashMap<>();
        String builder = getProjectClause() + getFixVersionClause() + getTypesClause() + getStatusClause();
        map.put("jql", builder);
        if (!fields().isEmpty()) {
            map.put("fields", fields());
        }

        return objectMapper.writeValueAsString(map);
    }

    private String getProjectClause() {
        return String.format("project=%s", jiraProject());
    }

    private String getFixVersionClause() {
        return String.format(" AND fixVersion='%s'", release());
    }

    private String getTypesClause() {
        if (issueTypes().isEmpty()) {
            return "";
        }
        return String.format(" AND type in (%s)", getCSVTokenStringWrappedWithQuote(issueTypes()));
    }

    private String getStatusClause() {
        if (statuses().isEmpty()) {
            return "";
        }
        return String.format(" AND status in (%s)", getCSVTokenStringWrappedWithQuote(statuses()));
    }

    private String getCSVTokenStringWrappedWithQuote(List<String> strings) {
        return strings.stream()
                .map(it -> "'" + it + "'")
                .collect(Collectors.joining(","));
    }
}
