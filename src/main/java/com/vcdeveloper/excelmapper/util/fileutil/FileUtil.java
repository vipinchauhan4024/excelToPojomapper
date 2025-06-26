package com.vcdeveloper.excelmapper.util.fileutil;

import java.io.InputStream;

/**
 * Util method related to File operation has to come under this class
 */
public final class FileUtil {

    private FileUtil() {

    }

    public static InputStream getInputStream(Class cls, String resorceName) {
        return cls.getClassLoader().getResourceAsStream(resorceName);

    }
}
