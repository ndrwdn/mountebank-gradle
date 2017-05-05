package org.ndrwdn.mbgradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class MountebankProjectSpec extends Specification {
    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()

    Project project

    def setup() {
        project = ProjectBuilder.builder()
                .withProjectDir(projectDir.root)
                .build()
        project.apply(plugin: MountebankGradlePlugin)
    }
}
