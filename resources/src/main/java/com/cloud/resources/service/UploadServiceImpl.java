package com.cloud.resources.service;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.resources.mapper.FileMapper;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class UploadServiceImpl implements UploadService{

    FileMapper fileMapper;


    public BlockingQueue<Integer> queue;

    public UploadServiceImpl() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) list.add(i);
        queue = new LinkedBlockingQueue(list);
    }



    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    private FileDB insert(String md5, String name) {
        FileDB fileDB = new FileDB();
        fileDB.setFilename(name); //随便填
        fileDB.setGmt_create(new Date());
        fileDB.setGmt_modified(new Date());
        fileDB.setMd5(md5);
        String path = FileUtil.get("/test"); // --随便填
        fileDB.setPath(path);
        fileMapper.insert(fileDB);
        return fileDB;
    }

    private FileDB update(FileDB fileDB, String extension) {
        int id = fileDB.getId();
        int block = id / 100;
        FileDB secondUpdate = new FileDB();
        secondUpdate.setId(fileDB.getId());
        secondUpdate.setPath("/block-" + block);
        secondUpdate.setFilename(fileDB.getId() + extension);
        secondUpdate.setSize(fileDB.getSize());

        fileMapper.update(secondUpdate);

        String newPath = "/block-" + block + "/" + id + extension;

        fileDB.setPath(newPath);
        fileDB.setFilename(secondUpdate.getFilename());
        return fileDB;
    }

    @Transactional
    FileDB saveFile_(MultipartFile file) {
        String md5 = MD5.getMD5(file);
        String extension = FileUtil.getExtension(file.getOriginalFilename());

        FileDB fileDB = insert(md5, file.getOriginalFilename());
        int id = fileDB.getId();

        int block = id / 100;

        File dir = new File(FileUtil.get("block-" + block));
        if (!dir.exists()) dir.mkdirs();

        String newPath = "/block-" + block + "/" + id + extension;

        try {
            file.transferTo(Paths.get(FileUtil.get(newPath)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        long size = file.getSize();
        fileDB.setSize(size);
        return update(fileDB, extension);
    }

    @Override
    @Transactional
    public FileDB saveFile(MultipartFile file) {
        if (file == null) return null;

        String md5 = MD5.getMD5(file);

        String extension = FileUtil.getExtension(file.getOriginalFilename());

        List<FileDB> fileDBs = fileMapper.queryByMd5(md5);

        if (fileDBs == null || fileDBs.isEmpty()) return saveFile_(file);

        Integer id = null;
        try {
            id = queue.take();
            String cPath = FileUtil.get("/check/" + id + "/");
            String cRealPath = cPath + file.getOriginalFilename();

            File cFile = new File(cPath);
            if (!cFile.exists()) cFile.mkdirs();

            file.transferTo(new File(cRealPath));

            for (FileDB fileDB : fileDBs) {
                try {
                    String path = fileDB.getPath();
                    String realPath = FileUtil.get(path + "/" + fileDB.getFilename());

                    File tmpFile = new File(cPath + fileDB.getFilename());

                    FileUtil.copy(new FileInputStream(realPath), new FileOutputStream(tmpFile));

                    Boolean flag = FileUtil.compareFile(realPath, tmpFile.getPath());

                    FileUtil.copy(new FileInputStream(tmpFile), new FileOutputStream(realPath));

                    if (flag) System.out.println(fileDB + "相等！！");

                    if (flag) return fileDB;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }

            File file1 = new File(cRealPath);

            FileDB fileDB = insert(md5, file.getOriginalFilename());

            fileDB.setSize(file.getSize());

            int fid = fileDB.getId();
            int block = fid / 100;

            String newPath = "/block-" + block + "/" + fid + extension;

            String newRealPath = FileUtil.get(newPath);

            FileUtil.copy(new FileInputStream(file1), new FileOutputStream(newRealPath));

            return update(fileDB, extension);


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                FileUtil.refresh("check", id);
                queue.put(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
