package com.cloud.resources.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtil {
    public static final String baseUrl = "/Users/crf/cloud-disk/resources";
    public static String get(String path) {
        if (path.length() == 0) return baseUrl;
        if (path.charAt(0) == '/') return baseUrl + path;
        return baseUrl + "/" + path;
    }

    public static final File cache(File file, Integer id, String name) {

        String tmpStr = "tmp/" + id;

        File tmp = new File(get(tmpStr));
        if (!tmp.exists()) tmp.mkdirs();

        File file2 = new File(get("/" + tmpStr + "/" + name));

        try {
            if (file2.exists()) {
                log.warn("文件名重复");
                return null;
            }
            Path copy = Files.copy(file.toPath(), file2.toPath());
            new File(String.valueOf(copy)).renameTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file2;
    }

    public static void refresh(int id) {
        File file = new File(get("tmp/" + id));
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean copy(InputStream in, OutputStream out, int size) {

        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] buf = new byte[size];

        try {
            while (bis.read(buf) != -1) {
                out.write(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static boolean copy(InputStream in, OutputStream out) {
        return copy(in, out, 8192);
    }

    public static File getFile(File file, Integer id, String newName) {
        File res = cache(file, id, newName);
        refresh(id);
        return res;
    }

}
