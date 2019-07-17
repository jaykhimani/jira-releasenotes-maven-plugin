package com.jak.maven.model;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.jak.maven.ImmutableRequestParameters;
import com.jak.maven.RequestParameters;
import com.jak.maven.fetcher.ReleaseItemsFetcher;
import com.jak.maven.model.response.JiraSearchResponse;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "releasenotes")
public class JiraReleaseNotesMojo extends AbstractMojo {
    @Parameter(required = true)
    private String jiraUrl;

    @Parameter(required = true)
    private String jiraProject;

    @Parameter
    private String releaseName;

    @Parameter
    private List<String> statuses;

    @Parameter
    private List<String> issueTypes;

    @Parameter(defaultValue = "/target/test.md")
    private String releaseNotesFile;

    @Parameter
    private String jiraUser;

    @Parameter
    private String jiraUserPassword;

    private List<String> fields = Arrays.asList("id", "key", "summary");

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private ReleaseItemsFetcher releaseItemsFetcher = new ReleaseItemsFetcher();

    public void execute() throws MojoExecutionException {
        RequestParameters jiraRequestParameters = ImmutableRequestParameters.builder()
                .jiraUrl(jiraUrl)
                .user(jiraUser)
                .password(jiraUserPassword)
                .jiraProject(jiraProject)
                .release(releaseName)
                .addAllStatuses(statuses)
                .addAllIssueTypes(issueTypes)
                .addAllFields(fields)
                .build();

        try {
            JiraSearchResponse jiraSearchResponse = releaseItemsFetcher.fetchItems(jiraRequestParameters);
        } catch (IOException e) {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException("Error fetching release items", e);
        }
    }
}
