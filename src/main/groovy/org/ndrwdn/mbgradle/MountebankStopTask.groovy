package org.ndrwdn.mbgradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.*

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static org.ndrwdn.mbgradle.MbPathUtil.mbDirectory
import static org.ndrwdn.mbgradle.MbPathUtil.mbPidFile

class MountebankStopTask extends DefaultTask {

    public static final String NAME = 'stopMountebank'

    private ExecutorService executor = new ThreadPoolExecutor(1, 1, 500, MILLISECONDS, new SynchronousQueue<Runnable>())

    @TaskAction
    def start() {
        if (isStarted()) {
            new ProcessBuilder()
                    .command('/usr/bin/env', 'bash', 'mb', 'stop')
                    .directory(mbDirectory(project))
                    .start()

            waitForStopOrTimeout()
        }
    }

    private waitForStopOrTimeout() {
        try {
            executor.submit(new Callable() {
                @Override
                call() {
                    while (isStarted()) {
                        Thread.sleep(100)
                    }
                }
            })
            .get(project.mountebank.stopTimeout, MILLISECONDS)
        } catch (TimeoutException e) {
            throw new RuntimeException('Mountebank failed to stop.', e)
        }
    }

    private boolean isStarted() {
        mbPidFile(project).exists()
    }
}
