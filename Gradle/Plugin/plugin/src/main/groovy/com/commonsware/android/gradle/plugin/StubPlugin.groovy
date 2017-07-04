package com.commonsware.android.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class StubPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        def myTask = target.tasks.create('stubTask') << {
            def helper = new StubHelper();
            println helper.getMessage();
        }

        myTask.group = 'commonware'
        myTask.description = 'Do something useful'
    }
}