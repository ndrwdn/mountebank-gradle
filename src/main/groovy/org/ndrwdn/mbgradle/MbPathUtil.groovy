package org.ndrwdn.mbgradle

import org.gradle.api.Project

import java.nio.file.Path
import java.nio.file.Paths

import static org.ndrwdn.mbgradle.OsUtil.determineMbOs

public class MbPathUtil {

    public static Path mbPath(Project project) {
        Paths
                .get(project.projectDir.path)
                .resolve(project.mountebank.extractPath as String)
                .resolve(determineMbOs())
    }

    public static File mbDirectory(Project project) {
        mbPath(project)
                .toFile()
    }

    public static File mbPidFile(Project project) {
        mbPath(project)
                .resolve('mb.pid')
                .toFile()
    }
}
