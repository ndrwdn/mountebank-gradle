package org.ndrwdn.mbgradle.acquisition

import org.ndrwdn.mbgradle.MbPathUtil
import org.ndrwdn.mbgradle.MountebankProjectSpec

class AcquireTest extends MountebankProjectSpec {

    def "Should download and extract when acquiring Mountebank"() {
        given:
        Acquire acquire = new Acquire(project)

        when:
        acquire.acquire()

        then:
        MbPathUtil.mbDirectory(project).exists()
    }
}
