package org.ndrwdn.mbgradle.acquisition

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.ndrwdn.mbgradle.MbPathUtil
import org.ndrwdn.mbgradle.MountebankGradlePlugin
import spock.lang.Specification

class AcquireTest extends Specification {

    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()

    Project project

    def setup() {
        project = ProjectBuilder.builder()
                .withProjectDir(projectDir.root)
                .build()
    }

    def "Should download and extract when acquiring Mountebank"() {
        given:
        project.apply(plugin: MountebankGradlePlugin)
        Acquire acquire = new Acquire(project)

        when:
        acquire.acquire()

        then:
        MbPathUtil.mbDirectory(project).exists()
    }
}
