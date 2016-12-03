package com.github.yu000hong.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The implementation of Plugin
 */
class DBComparePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('dbCompareConfig', DBComparePluginExtension)
        project.tasks.create('dbCompare', DBCompareTask)
    }

}
