package com.cloud.resources.service;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.resources.mapper.FileMapper;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService{

    FileMapper fileMapper;

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    @Transactional
    public FileDB moveFile(String path1) {
        File file1 = new File(FileUtil.get(path1));

        System.out.println("===========moveFile=================");
        System.out.println("========>" + file1);

        FileDB fileDB = new FileDB();
        fileDB.setGmt_create(new Date());
        fileDB.setGmt_modified(new Date());
        fileDB.setFilename(file1.getName());
        fileDB.setSize(file1.length());
        fileDB.setMd5(MD5.getMD5(file1));
        fileDB.setPath("/");

        fileMapper.insert(fileDB);

        Integer id = fileDB.getId();

        int block = id / 100;

        String path2 = "/block-" + block;

        File path = new File(FileUtil.get(path2));
        if (!path.exists()) path.mkdirs();

        String name = id + FileUtil.getExtension(file1.getName());

        System.out.println("name = " + name);

        File file2 = new File(path, name);

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file2))) {
            byte[] bytes = new byte[1024 * 1024];

            int len = 0;

            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                bos.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        file1.delete();

        fileDB.setFilename(name);
        fileDB.setGmt_modified(new Date());
        fileDB.setPath(path2);

        fileMapper.update(fileDB);

        return fileDB;
    }
}
