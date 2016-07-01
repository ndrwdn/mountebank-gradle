package org.ndrwdn.mbgradle

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.GZIPInputStream

import static FileModeUtil.isExecutable

class MountebankAcquisitionTask extends DefaultTask {

    @TaskAction
    def acquire() {
        def http = new HTTPBuilder('https://s3.amazonaws.com')
        http.request(Method.GET, ContentType.BINARY) { req ->
            uri.path = '/mountebank/v1.5/mountebank-v1.5.1-darwin-x64.tar.gz'

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

                Files.move(mbDownloadPath.resolve('mountebank-v1.5.1-darwin-x64'), Paths.get(project.mountebank.extractPath))
            }
        }
    }
}
