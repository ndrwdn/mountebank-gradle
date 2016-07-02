package org.ndrwdn.mbgradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class MountebankGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('mountebank', MountebankPluginExtension)

        project.task(MountebankAcquisitionTask.NAME, type: MountebankAcquisitionTask) {
            outputs.upToDateWhen {
                new File(project.mountebank.extractPath as String).exists()
            }
        }

        project.task(MountebankStartTask.NAME, type: MountebankStartTask) {
            dependsOn MountebankAcquisitionTask.NAME
            outputs.upToDateWhen {
                new File(project.mountebank.extractPath as String, 'mb.pid').exists()
            }
        }

    }
}
