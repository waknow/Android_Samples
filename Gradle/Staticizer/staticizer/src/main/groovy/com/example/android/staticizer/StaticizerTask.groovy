package com.example.android.staticizer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

public class StaticizerTask extends DefaultTask {
    @InputDirectory
    File inputDir = new File(project.getProjectDir(),"src/main/staticizer")
    @OutputDirectory
    File outputDir;

    @TaskAction
    public void execute(IncrementalTaskInputs inputs){
        if (!project.staticizer.packageName) {
            throw new IllegalStateException('staticizer.packageName is undefined')
        }

        def staticizer=  new Staticizer()

        for (File input : inputDir.listFiles()) {
            if (!input.name.startsWith('.')) {
                staticizer.generate(input, outputDir, project.staticizer.packageName)
            }
        }
    }
}