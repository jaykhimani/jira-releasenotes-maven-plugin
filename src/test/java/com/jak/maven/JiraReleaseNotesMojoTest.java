package com.jak.maven;


import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;

public class JiraReleaseNotesMojoTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {}

        @Override
        protected void after() {}
    };
}

