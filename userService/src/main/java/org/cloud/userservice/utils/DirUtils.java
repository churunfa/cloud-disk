package org.cloud.userservice.utils;

public class DirUtils {
    public static Boolean checkPath(String path) {
        if (path == null || path.length() == 0) return false;
        if (path.charAt(0) != '/') return false;
        if (path.charAt(path.length() - 1) != '/') return false;
        return true;
    }

    public static Boolean checkName(String name) {
        if (name == null || name.length() == 0) return false;
        for (int i = 0; i < name.length(); i++) if (name.charAt(i) == '/') return false;
        return true;
    }
}
