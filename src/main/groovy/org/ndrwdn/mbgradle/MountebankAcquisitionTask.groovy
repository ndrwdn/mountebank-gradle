package org.ndrwdn.mbgradle

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.util.zip.GZIPInputStream

import static groovyx.net.http.ContentType.BINARY
import static groovyx.net.http.Method.GET
import static org.ndrwdn.mbgradle.FileModeUtil.isExecutable
import static org.ndrwdn.mbgradle.MbPathUtil.mbPath
import static org.ndrwdn.mbgradle.OsUtil.determineMbOs

class MountebankAcquisitionTask extends DefaultTask {

    public static final String NAME = 'acquireMountebank'
    public static final String MB_MAJOR_VERSION = '1.9'
    public static final String MB_MINOR_VERSION = '0'
    public static final String MB_FULL_VERSION = "$MB_MAJOR_VERSION.$MB_MINOR_VERSION"

    @TaskAction
    def acquire() {
        def http = new HTTPBuilder('https://s3.amazonaws.com')
        http.request(GET, BINARY) { req ->
            uri.path = "/mountebank/v$MB_MAJOR_VERSION/mountebank-v$MB_FULL_VERSION-${determineMbOs()}-x64.tar.gz"

            response.success = { HttpResponseDecorator resp, InputStream content ->
                def mbDownloadPath = project
                        .buildDir
                        .toPath()
                        .resolve('tmp')
                mbDownloadPath.toFile().mkdirs()

                def mbTar = new TarArchiveInputStream(new GZIPInputStream(content))
                for (TarArchiveEntry entry = mbTar.nextTarEntry; entry != null; entry = mbTar.nextTarEntry) {
                    def extractFile = mbDownloadPath
                            .resolve(entry.name)
                            .toFile()

                    if (entry.directory) {
                        extractFile
                                .mkdirs()
                    } else {
                        extractFile
                                .parentFile
                                .mkdirs()
                        extractFile
                                .createNewFile()
                        extractFile.setExecutable(isExecutable(entry.mode), false)

                        def entryOs = new BufferedOutputStream(new FileOutputStream(extractFile))
                        def readBuffer = new byte[1024]
                        int bytesRead
                        while ((bytesRead = mbTar.read(readBuffer)) != -1) {
                            entryOs.write(readBuffer, 0, bytesRead)
                        }
                        entryOs.close()
                    }
                }

                mbPath(project)
                        .toFile()
                        .parentFile
                        .mkdirs()
                Files.move(
                        mbDownloadPath.resolve("mountebank-v$MB_FULL_VERSION-${determineMbOs()}-x64"),
                        mbPath(project))
            }
        }
    }
}
