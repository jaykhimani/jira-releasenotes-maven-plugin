# Release notes from Jira

## Overview
[Atlassian Jira](https://www.atlassian.com/software/jira) is one of the leading and favorites when it comes to issue tracking
and with the rise of Agile, also backlog management. Issue tracking of Jira makes it a lot easy to manage release and track
what went in which release. Just tag the issues, stories etc to Jira's release and there you have your release notes. Easy right?

But what if you want to automate the release notes generation for each release you make? What if you want only completed stories 
and issues to be part of release notes and not the impeded or incomplete ones? 

Answer is Jira's reach REST APIs and maven plugins. Idea is to invoke Jira's APIs during your release preparation, fetch the 
items which are interested in, create release notes in your favourite format (markdown for example) and budle it as one of the
artifacts along with others like binary, javadoc etc. 

This repo demonstrates the same. 
1. Include `jira-releasenotes-maven-plugin` in your build life cycle
   ```
   <groupId>com.jak.maven</groupId>
   <artifactId>jira-releasenotes-maven-plugin</artifactId>
   <version>1.0-SNAPSHOT</version>
   ``` 
2. Provide certain configuration (see Example usage for more details)
3. Bind the plugin execution to your desired maven phase. `install` for example.

**Note**: This project uses [immutables](https://immutables.github.io/), so after cloning ensure your IDE is configured to support
annotation processing. For [more details refer this](https://immutables.github.io/apt.html). 

### Creating skeleton maven plugin project
```
mvn archetype:generate -DgroupId=com.jak.maven -DartifactId=jira-releasenotes-maven-plugin -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-plugin
```