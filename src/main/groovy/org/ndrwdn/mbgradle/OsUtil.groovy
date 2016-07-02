package org.ndrwdn.mbgradle

class OsUtil {

    private static final Map<String, String> osMappings = [
            'Linux': 'linux',
            'Mac OS X': 'darwin'
    ]

    public static String determineMbOs() {
        osMappings.get(System.getProperty('os.name'))
    }
}
