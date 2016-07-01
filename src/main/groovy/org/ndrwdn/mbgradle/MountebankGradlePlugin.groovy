package org.ndrwdn.mbgradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class MountebankGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('mountebank', MountebankPluginExtension)
        project.task('acquireMountebank', type: MountebankAcquisitionTask)
    }
}
