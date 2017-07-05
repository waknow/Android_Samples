package com.example.android.staticizer

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class StaticizerPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        def isApp = target.plugins.hasPlugin AppPlugin
        def isLib = target.plugins.hasPlugin LibraryPlugin

        if (!isApp && !isLib) {
            throw new IllegalStateException("This plugin depends upon the com.android.application or com.android.library plugins.")
        }
        target.extensions.create('staticizer', StaticizerConfig)

        def variants
        if (isApp) {
            variants = target.android.applicationVariants
        } else {
            variants = target.android.libraryVariants
        }

        variants.all { variant ->
            def taskName = "staticizer${variant.name.capitalize()}"
            def task = target.tasks.create(taskName, StaticizerTask)
            task.outputDir = new File("${target.buildDir}/generated/source/staticizer/${variant.name}")
            task.group = 'jove'
            task.description = "Generate ${variant.name} Java code from JSON"

            variant.javaCompile.dependsOn task
            variant.registerJavaGeneratingTask task, task.outputDir
        }
    }
}

class StaticizerConfig {
    def String packageName
}