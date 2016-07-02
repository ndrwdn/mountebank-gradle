package org.ndrwdn.mbgradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.*

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static java.util.concurrent.TimeUnit.SECONDS

class MountebankStopTask extends DefaultTask {

    public static final String NAME = 'stopMountebank'

    private ExecutorService executor = new ThreadPoolExecutor(1, 1, 500, MILLISECONDS, new SynchronousQueue<Runnable>())

    @TaskAction
    def start() {
        if (isStarted()) {
            new ProcessBuilder()
                    .command('/usr/bin/env', 'bash', 'mb', 'stop')
                    .directory(new File(project.mountebank.extractPath as String))
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
            .get(5, SECONDS)
        } catch (TimeoutException e) {
            throw new RuntimeException('Mountebank failed to stop.', e)
        }
    }

    private boolean isStarted() {
        new File(project.mountebank.extractPath as String, 'mb.pid').exists()
    }
}
