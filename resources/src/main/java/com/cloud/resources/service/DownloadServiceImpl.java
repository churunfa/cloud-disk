package com.cloud.resources.service;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.resources.mapper.FileMapper;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DownloadServiceImpl implements DownloadService {

    public BlockingQueue<Integer> queue;

    FileMapper fileMapper;

    public void setQueue(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public DownloadServiceImpl() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) list.add(i);
        queue = new LinkedBlockingQueue(list);
    }

    @Override
    public ResponseEntity<byte[]> downloadByPath(Integer id, String filename) {
        FileDB fileDB = fileMapper.queryFileById(id);
        String path = fileDB.getPath() + "/" + fileDB.getFilename();

        path = FileUtil.get(path);
        return downloadByPath(path, filename);
    }

    public DownloadServiceImpl(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public ResponseEntity<byte[]> downloadByPath(String path, String filename) {

        ResponseEntity<byte[]> entity;
        File file = new File(path);
        byte[] body = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            body = new byte[is.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();

        try {
            filename = URLEncoder.encode(filename,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        headers.setContentDispositionFormData("attachment", filename);

        HttpStatus statusCode = HttpStatus.OK;
        entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }

    @Override
    public ResponseEntity<byte[]> downloadByPaths(ZipFile zipFile) {
        System.out.println(zipFile);
        String zipName = zipFile.getZipName();
        ResponseEntity<byte[]> entity = null;
        Integer take = null;
        try {
            take = queue.take();

            Map<String, String> files = zipFile.getFiles();
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String path = FileUtil.get(entry.getKey());
                String name = entry.getValue();
                FileUtil.cache("tmp", new File(path), take, name);
            }

            String realPath = FileUtil.get("/tmp/" + take);

            ZipUtils.toZip_(realPath, zipName);

            entity = downloadByPath(realPath + "/" + zipName, zipName);

            FileUtil.refresh("tmp", take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                queue.put(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
