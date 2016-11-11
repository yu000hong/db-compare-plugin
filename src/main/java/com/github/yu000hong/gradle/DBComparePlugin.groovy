package com.github.yu000hong.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class DBComparePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create('dbCompareConfig', DBComparePluginExtension)
        project.getTasks().create('dbCompare', DBCompareTask)
    }

}
