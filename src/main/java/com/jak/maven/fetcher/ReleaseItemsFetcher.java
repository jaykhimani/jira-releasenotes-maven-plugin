package com.jak.maven.fetcher;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jak.maven.RequestParameters;
import com.jak.maven.model.response.JiraSearchResponse;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReleaseItemsFetcher {
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String APPLICATION_JSON = "application/json";
    private static final MediaType APP_JSON = MediaType.parse(APPLICATION_JSON);

    public JiraSearchResponse fetchItems(RequestParameters requestParameters) throws IOException {
        String basicAuth = Credentials.basic(requestParameters.user(), requestParameters.password());
        String jql = requestParameters.buildJql();
        RequestBody body = RequestBody.create(APP_JSON, jql);

        Request request = new Request.Builder().url(requestParameters.jiraUrl() + "/rest/api/2/search")
                .header("Authorization", basicAuth)
                .header("Accept", APPLICATION_JSON)
                .header("Content-type", APPLICATION_JSON)
                .post(body)
                .build();

        Response response = client.newCall(request)
                .execute();
        String output = response.body()
                .string();
        return objectMapper.readValue(output, JiraSearchResponse.class);

    }
}
