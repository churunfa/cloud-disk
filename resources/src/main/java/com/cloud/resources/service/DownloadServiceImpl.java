package com.cloud.resources.service;

import com.cloud.common.pojo.file.ZipFile;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.ZipUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DownloadServiceImpl implements DownloadService {

    public BlockingQueue<Integer> queue;

    public DownloadServiceImpl() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) list.add(i);
        queue = new LinkedBlockingQueue(list);
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
            headers.setContentDispositionFormData("attachment", new String(filename.getBytes("UTF-8"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpStatus statusCode = HttpStatus.OK;
        entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }

    @Override
    public ResponseEntity<byte[]> downloadByPaths(ZipFile zipFile) {
        String zipName = zipFile.getZipName();
        ResponseEntity<byte[]> entity = null;
        try {
            Integer take = queue.take();
            zipFile.getFiles().forEach((String path, String name)->{
                FileUtil.cache(new File(path), take, name);
            });

            String realPath = FileUtil.get("/tmp/" + take);

            ZipUtils.toZip_(realPath, zipName);

            entity = downloadByPath(realPath + "/" + zipName, zipName);

            FileUtil.refresh(take);
            queue.put(take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
