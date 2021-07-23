package org.cloud.uploadanddownload.utils;

public class FileUtil {
    public static final String baseUrl = "/Users/crf/cloud-disk/download";

    public static String get(String path) {
        if (path.length() == 0) return baseUrl;
        if (path.charAt(0) == '/') return baseUrl + path;
        return baseUrl + "/" + path;
    }

}
