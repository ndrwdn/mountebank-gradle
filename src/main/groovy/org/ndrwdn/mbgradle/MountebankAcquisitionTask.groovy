package org.ndrwdn.mbgradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.ndrwdn.mbgradle.acquisition.Acquire

class MountebankAcquisitionTask extends DefaultTask {

    public static final String NAME = 'acquireMountebank'

    @TaskAction
    def acquire() {
        new Acquire(project).acquire()
    }
}
