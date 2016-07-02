package org.ndrwdn.mbgradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.*

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static java.util.concurrent.TimeUnit.SECONDS
import static org.ndrwdn.mbgradle.MbPathUtil.mbDirectory
import static org.ndrwdn.mbgradle.MbPathUtil.mbPidFile

class MountebankStartTask extends DefaultTask {

    public static final String NAME = 'startMountebank'

    private ExecutorService executor = new ThreadPoolExecutor(1, 1, 500, MILLISECONDS, new SynchronousQueue<Runnable>())

    @TaskAction
    def start() {
        if (isStopped()) {
            new ProcessBuilder()
                    .command('/usr/bin/env', 'bash', 'mb')
                    .directory(mbDirectory(project))
                    .start()

            waitForStartOrTimeout()
        }
    }

    private waitForStartOrTimeout() {
        try {
            executor.submit(new Callable() {
                @Override
                call() {
                    while (isStopped()) {
                        Thread.sleep(100)
                    }
                }
            })
            .get(5, SECONDS)
        } catch (TimeoutException e) {
            throw new RuntimeException('Mountebank failed to start.', e)
        }
    }

    private boolean isStopped() {
        !mbPidFile(project).exists()
    }
}
