package com.jak.maven.fetcher;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jak.maven.model.request.ImmutableRequestParameters;
import com.jak.maven.model.request.RequestParameters;
import com.jak.maven.model.response.Fields;
import com.jak.maven.model.response.Issue;
import com.jak.maven.model.response.JiraSearchResponse;

import okhttp3.Credentials;
import okhttp3.MediaType;

public class ReleaseItemsFetcherTest {
    private static final String APPLICATION_JSON = "application/json";
    private static final MediaType APP_JSON = MediaType.parse(APPLICATION_JSON);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig()
            .port(8083));

    @Test
    public void fetchItems() throws IOException {
        RequestParameters reqBody = ImmutableRequestParameters.builder()
                .jiraProject("MYPROJ")
                .jiraUrl("http://localhost:8083/jira")
                .user("user")
                .password("password")
                .addAllFields(Arrays.asList("id", "key", "summary"))
                .addAllIssueTypes(Arrays.asList("Story", "Issue"))
                .addAllStatuses(Arrays.asList("Closed", "Done"))
                .release("1.0-SNAPSHOT")
                .build();

        String jql = reqBody.buildJql();
        Issue issue = new Issue();
        issue.setId("0001");
        issue.setFields(new Fields().withSummary("Issue 1"));
        issue.setKey("MYPROG-0001");
        issue.setSelf("http://localhost:8083/jira/rest/api/2/issue/0001");

        JiraSearchResponse resp = new JiraSearchResponse();
        resp.setIssues(Collections.singletonList(issue));
        resp.setMaxResults(10);
        resp.setStartAt(0);
        resp.setTotal(6);


        stubFor(post(urlEqualTo("/jira/rest/api/2/search")).withHeader("Authorization", equalTo(Credentials.basic("user", "password")))
                .withHeader("Accept", equalTo(APPLICATION_JSON))
                .withHeader("Content-type", equalTo("application/json; charset=UTF-8"))
                .withRequestBody(equalToJson(jql))
                .willReturn(aResponse().withStatus(200)
                        .withBody(objectMapper.writeValueAsString(resp))));

        ReleaseItemsFetcher fetch = new ReleaseItemsFetcher();

        JiraSearchResponse jiraSearchResponse = fetch.fetchItems(reqBody);
        assertNotNull(jiraSearchResponse);
        verify(1, postRequestedFor(urlEqualTo("/jira/rest/api/2/search")));
    }
}
