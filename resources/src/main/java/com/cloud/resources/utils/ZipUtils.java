package com.cloud.resources.utils;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
/**
 * 解压Zip文件工具类
 * @author zhangyongbo
 *
 */
public abstract class ZipUtils {

    /**
     * 压缩成ZIP 方法1
     *
     * @param src 压缩文件夹路径
     * @param out 压缩文件输出流
     */

    public static void toZip(String src, String out) {

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(out))) {
            File file = new File(src);
            compress(file, zos, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 压缩成ZIP 方法1
     *
     * @param src 压缩文件夹路径
     * @param out 压缩文件输出流
     */

    public static void toZip(String src, OutputStream out) {

        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            File file = new File(src);
            compress(file, zos, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 压缩成ZIP 方法2
     *
     * @param files 需要压缩的文件列表
     * @param out   压缩文件输出流
     */

    public static void toZip(List<File> files, OutputStream out) {

        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File srcFile : files) {
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                FileInputStream in = new FileInputStream(srcFile);
                FileUtil.copy(in, zos);
                zos.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 递归压缩方法
     *
     * @param file 源文件
     * @param zos  zip输出流
     * @param name 压缩后的名称
     */

    private static void compress(File file, ZipOutputStream zos, String name) throws Exception {

        if (file.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            WritableByteChannel writableByteChannel = Channels.newChannel(zos);
            FileInputStream in = new FileInputStream(file);
            FileChannel fileChannel = in.getChannel();
            fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
//            FileUtil.copy(in, zos);
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                zos.putNextEntry(new ZipEntry(name + File.separator));
                zos.closeEntry();
            } else {
                for (File target : listFiles) {
                    compress(target, zos, name + File.separator + target.getName());
                }
            }
        }
    }

    public static void toZip_(String src, String name) {

        File[] files = new File(src).listFiles();

        String out = src + "/" + name;

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(out))) {
            for (File file : files) {
                compress(file, zos, file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
