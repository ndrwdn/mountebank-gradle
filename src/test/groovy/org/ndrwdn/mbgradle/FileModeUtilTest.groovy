package org.ndrwdn.mbgradle

import spock.lang.Specification

class FileModeUtilTest extends Specification {

    def 'executable bit is set'(int mode, boolean executable) {
        expect:
        FileModeUtil.isExecutable(mode) == executable

        where:
        mode | executable
        0000 | false
        0001 | true
        0010 | true
        0100 | true
        0002 | false
        0020 | false
        0200 | false
        0003 | true
        0030 | true
        0300 | true
        0004 | false
        0040 | false
        0400 | false
        0005 | true
        0050 | true
        0500 | true
        0006 | false
        0060 | false
        0600 | false
        0007 | true
        0070 | true
        0700 | true
    }
}
