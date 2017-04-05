package org.ndrwdn.mbgradle

import org.gradle.api.Project

public class MbArgUtil {

    public static List<String> buildCliArguments(Project project) {
        [new Argument('port',           project.mountebank.port),
         new Argument('configFile',     project.mountebank.configFile),
         new Argument('noParse',        project.mountebank.noParse),
         new Argument('logFile',        project.mountebank.logFile),
         new Argument('logLevel',       project.mountebank.logLevel),
         new Argument('noLogFile',      project.mountebank.noLogFile),
         new Argument('allowInjection', project.mountebank.allowInjection),
         new Argument('localOnly',      project.mountebank.localOnly),
         new Argument('ipWhitelist',    project.mountebank.ipWhitelist),
         new Argument('mock',           project.mountebank.mock),
         new Argument('debug',          project.mountebank.debug),
         new Argument('pidFile',        project.mountebank.pidFile),
         new Argument('saveFile',       project.mountebank.saveFile),
         new Argument('removeProxies',  project.mountebank.removeProxies)]
                .collect { arg -> arg.resolve() }
                .flatten() as List<String>
    }

    private static class Argument {
        String key
        Object value

        Argument(String key, Object value) {
            this.key = key
            this.value = value
        }

        List<String> resolve() {
            value == null ? [] : ["--$key", value.toString()]
        }
    }
}
