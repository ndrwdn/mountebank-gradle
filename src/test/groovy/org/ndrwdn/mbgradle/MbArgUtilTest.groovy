package org.ndrwdn.mbgradle

class MbArgUtilTest extends MountebankProjectSpec {

    void 'should return arguments reflecting the mb configuration'() {
        given:
        project.mountebank.port = 2626
        project.mountebank.configFile = null
        project.mountebank.noParse = true
        project.mountebank.logFile = 'log_file.log'
        project.mountebank.allowInjection = false

        when:
        List<String> cliArgs = MbArgUtil.buildCliArguments(project)

        then:
        cliArgs == [
                '--port', '2626',
                '--noParse', 'true',
                '--logFile', 'log_file.log',
                '--allowInjection', 'false']
    }

    void 'should return all mb config arguments'() {
        given:
        project.mountebank.port = 2525
        project.mountebank.configFile = 'imposters.ejs'
        project.mountebank.noParse = false
        project.mountebank.logFile = 'mb.log'
        project.mountebank.logLevel = 'info'
        project.mountebank.noLogFile = false
        project.mountebank.allowInjection = false
        project.mountebank.localOnly = false
        project.mountebank.ipWhitelist = '*'
        project.mountebank.mock = false
        project.mountebank.debug = false
        project.mountebank.pidFile = 'mb.pid'
        project.mountebank.saveFile = 'mb.json'
        project.mountebank.removeProxies = false

        when:
        List<String> cliArgs = MbArgUtil.buildCliArguments(project)

        then:
        cliArgs == ['--port', '2525',
                    '--configFile', 'imposters.ejs',
                    '--noParse', 'false',
                    '--logFile', 'mb.log',
                    '--logLevel', 'info',
                    '--noLogFile', 'false',
                    '--allowInjection', 'false',
                    '--localOnly', 'false',
                    '--ipWhitelist', '*',
                    '--mock', 'false',
                    '--debug', 'false',
                    '--pidFile', 'mb.pid',
                    '--saveFile', 'mb.json',
                    '--removeProxies', 'false']

    }
}
