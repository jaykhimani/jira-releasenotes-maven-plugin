package com.jak.maven.fetcher;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.jak.maven.model.request.ImmutableRequestParameters;
import com.jak.maven.model.request.RequestParameters;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        StubMapping stubMapping =
                stubFor(post(urlEqualTo("/jira/rest/api/2/search")).withHeader("Authorization", equalTo(Credentials.basic("user", "password")))
                        .withHeader("Accept", equalTo(APPLICATION_JSON))
                        .withHeader("Content-type", equalTo(APPLICATION_JSON))
                        .withRequestBody(equalToJson("\"jql\":" + jql))
                        .willReturn(aResponse().withStatus(200)));


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8083/jira/rest/api/2/search");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");
        request.addHeader("Authorization", Credentials.basic("user", "password"));
        request.setEntity(new StringEntity("\"jql\": " + jql));
        HttpResponse response = httpClient.execute(request);
        assertNotNull(response);

        RequestBody body = RequestBody.create(APP_JSON, jql);

        Request req = new Request.Builder().url(reqBody.jiraUrl() + "/rest/api/2/search")
                .header("Authorization", Credentials.basic("user", "password"))
                .header("Accept", APPLICATION_JSON)
                .header("Content-type", APPLICATION_JSON)
                .post(body)
                .build();

        Response resp = new OkHttpClient().newCall(req)
                .execute();

        assertNotNull(resp);
        assertTrue(resp.isSuccessful());



        // ReleaseItemsFetcher fetch = new ReleaseItemsFetcher();
        //
        //
        // JiraSearchResponse jiraSearchResponse = fetch.fetchItems(reqBody);
        // assertNotNull(jiraSearchResponse);
    }
}
