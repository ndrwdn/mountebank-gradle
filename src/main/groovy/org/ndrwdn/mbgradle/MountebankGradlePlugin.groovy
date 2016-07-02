package org.ndrwdn.mbgradle

import org.gradle.api.Plugin
import org.gradle.api.Project

import static org.ndrwdn.mbgradle.MbPathUtil.mbDirectory
import static org.ndrwdn.mbgradle.MbPathUtil.mbPidFile

class MountebankGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('mountebank', MountebankPluginExtension)

        project.task(MountebankAcquisitionTask.NAME, type: MountebankAcquisitionTask) {
            outputs.upToDateWhen {
                mbDirectory(project).exists()
            }
        }

        project.task(MountebankStartTask.NAME, type: MountebankStartTask) {
            dependsOn MountebankAcquisitionTask.NAME
            outputs.upToDateWhen {
                mbPidFile(project).exists()
            }
        }


        project.task(MountebankStopTask.NAME, type: MountebankStopTask) {
            dependsOn MountebankAcquisitionTask.NAME
            outputs.upToDateWhen {
                !mbPidFile(project).exists()
            }
        }

    }
}
