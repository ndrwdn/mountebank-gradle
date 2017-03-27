package org.ndrwdn.mbgradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.*

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static org.ndrwdn.mbgradle.MbPathUtil.mbDirectory
import static org.ndrwdn.mbgradle.MbPathUtil.mbPidFile

class MountebankStartTask extends DefaultTask {

    public static final String NAME = 'startMountebank'

    private ExecutorService executor = new ThreadPoolExecutor(1, 1, 500, MILLISECONDS, new SynchronousQueue<Runnable>())

    @TaskAction
    def start() {
        if (isStopped()) {
            new ProcessBuilder()
                    .command(mbCommand())
                    .directory(mbDirectory(project))
                    .start()

            waitForStartOrTimeout()
        }
    }

    private List<String> mbCommand() {
        List<String> command = ['/usr/bin/env', 'bash', 'mb']
        if (project.mountebank.allowInjection) {
            command << '--allowInjection'
        }
        command
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
            .get(project.mountebank.startTimeout, MILLISECONDS)
        } catch (TimeoutException e) {
            throw new RuntimeException('Mountebank failed to start.', e)
        }
    }

    private boolean isStopped() {
        !mbPidFile(project).exists()
    }
}
