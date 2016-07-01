package org.ndrwdn.mbgradle

class FileModeUtil {

    static boolean isExecutable(int mode) {
        (mode & 0001) == 1 || (mode & 0010) == 0010 || (mode & 0100) == 0100
    }
}
