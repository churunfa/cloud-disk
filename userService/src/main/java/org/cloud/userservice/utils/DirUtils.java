package org.cloud.userservice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static Boolean checkSuf(String name) {
        for (int i = 0; i < name.length(); i++) if (name.charAt(i) == '.') return true;
        return false;
    }

    public static String getRecycleName(String name) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        String data = sdf.format(new Date());

        if (!checkSuf(name)) return name + "-" + data ;

        String[] split = name.split("\\.");

        StringBuffer stringBuffer = new StringBuffer("");

        for (int i = 0; i < split.length - 1; i++) stringBuffer.append(split[i]);

        stringBuffer.append(" ").append(data).append(".").append(split[split.length - 1]);

        return stringBuffer.toString();
    }
}
