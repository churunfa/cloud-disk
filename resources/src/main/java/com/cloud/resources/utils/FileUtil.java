package com.cloud.resources.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtil {
    public static final String baseUrl = "/Users/crf/cloud-disk/resources";

    //    public static final String baseUrl = "/root/cloud-disk/resources";
    public static String get(String path) {
        if (path.length() == 0) return baseUrl;
        if (path.charAt(0) == '/') return baseUrl + path;
        return baseUrl + "/" + path;
    }

    public static final File cache(String pre, File file, Integer id, String name) {

        String tmpStr = pre + "/" + id;

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

    public static void refresh(String pre, int id) {
        File file = new File(get(pre + "/" + id));
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
            int len = 0;
            while ((len = bis.read(buf)) != -1) {
                out.write(buf, 0, len);
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
        return copy(in, out, 5 * 1024 * 1024);
    }

    public static File getFile(File file, Integer id, String newName) {
        File res = cache("tmp", file, id, newName);
        refresh("tmp", id);
        return res;
    }

    public static String getExtension(String name) {
        String fileType = name.substring(name.lastIndexOf("."));
        return fileType;
    }

    private static Boolean check(BufferedInputStream fir, BufferedInputStream sec) throws IOException {

        byte[] firBuff = new byte[1024 * 1024];
        byte[] secBuff = new byte[1024 * 1024];
        int firLen = 0;
        int secLen = 0;
        if (fir.available() == sec.available()) {
            while ((firLen = fir.read(firBuff)) != -1 && (secLen = sec.read(secBuff)) != -1) {
                if (firLen != secLen) {
                    return false;
                }
                for (int i = 0; i < firLen; i++) if (firBuff[i] != secBuff[i]) return false;
            }
            return true;
        } else {
            return false;
        }
    }


    public static Boolean compareFile(String firFile, String secFile) throws IOException {
        BufferedInputStream fir = null;
        BufferedInputStream sec = null;
        try {
            fir = new BufferedInputStream(new FileInputStream(firFile));
            sec = new BufferedInputStream(new FileInputStream(secFile));
            //比较文件的长度是否一样
            return check(fir, sec);

        } finally {
            if (fir != null) {
                fir.close();
            }
            if (sec != null) {
                sec.close();
            }
        }
    }

    public static Boolean compareFile(String firFile, InputStream sec_) throws IOException {
        BufferedInputStream fir = null;
        BufferedInputStream sec = null;
        try {
            fir = new BufferedInputStream(new FileInputStream(firFile));
            sec = new BufferedInputStream(sec_);
            return check(fir, sec);

        } finally {
            if (fir != null) {
                fir.close();
            }
            if (sec != null) {
                sec.close();
            }
        }
    }

    public static Boolean merge(String f1, String f2) {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f1));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f2, true));) {

            byte[] bytes = new byte[1024 * 1024];

            int len = 0;

            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }
}
