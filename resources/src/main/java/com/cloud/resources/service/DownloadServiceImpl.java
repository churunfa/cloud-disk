package com.cloud.resources.service;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.common.pojo.file.ZipFileChunk;
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
import java.util.concurrent.ConcurrentHashMap;
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


    private RestResult<byte[]> getChunkData(String path, int chunkSize, int chunkNo) {

        byte[] bytes = new byte[chunkSize];

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))) {

            bis.skip(chunkNo * chunkSize);
            int len = bis.read(bytes);
            return RestResultUtils.success(len, bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RestResultUtils.failed("读取文件失败");
    }

    @Override
    public RestResult<byte[]> chunkDownload(int fid, int chunkSize, int chunkNo) {
        FileDB fileDB = fileMapper.queryFileById(fid);
        String path = fileDB.getPath();
        String filename = fileDB.getFilename();
        Long size = fileDB.getSize();

        Long count = (size + chunkSize - 1) / chunkSize;

        if (chunkNo >= count) return RestResultUtils.failed("没有当前块");

        String realPath = FileUtil.get(path + "/" + filename);

        return getChunkData(realPath, chunkSize, chunkNo);
    }

    Map<String, ZipFileChunk> map = new ConcurrentHashMap<>();

    @Override
    public RestResult<long[]> getZip(ZipFileChunk zipFile) {
        System.out.println(zipFile);
        String zipName = zipFile.getZipName();
        Integer take = null;
        try {
            take = queue.take();

            Map<String, String> files = zipFile.getFiles();
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String path = FileUtil.get(entry.getValue());
                String name = entry.getKey();
                FileUtil.cache("tmp", new File(path), take, name);
            }

            String realPath = FileUtil.get("/tmp/" + take);

            ZipUtils.toZip_(realPath, zipName);
            long[] longs = new long[2];
            longs[0] = take;
            longs[1] = new File(realPath + "/" + zipName).length();
            zipFile.setTask(take);
            zipFile.setTotSize(longs[1]);
            map.put(zipFile.getId(), zipFile);
            return RestResultUtils.success(longs);
        } catch (InterruptedException e) {
            e.printStackTrace();
            try {
                queue.put(take);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return RestResultUtils.failed();
        }
    }


    @Override
    public RestResult<byte[]> chunkDownloadByPaths(ZipFileChunk zipFile) {
        String realPath = FileUtil.get("/tmp/" + zipFile.getTask() + "/" + zipFile.getZipName());
        Integer chunkNo = zipFile.getChunkNo();
        Integer chunkSize = zipFile.getChunkSize();
        Long totSize = zipFile.getTotSize();
        System.out.println(totSize);
        long count = (totSize + chunkSize - 1) / chunkSize;

        if (chunkNo >= count) RestResultUtils.failed("不存在的块");

        byte[] bytes = new byte[5 * 1024 * 1024];

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath))) {
            bis.skip(chunkNo * chunkSize);
            int len = bis.read(bytes);
            return RestResultUtils.success(len, bytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RestResultUtils.failed();
    }

    @Override
    public RestResult deleteZip(int takeId) {
        FileUtil.refresh("tmp", takeId);
        try {
            map.remove(takeId);
            queue.put(takeId);
            return RestResultUtils.success();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return RestResultUtils.failed();
        }
    }

    @Override
    public ZipFileChunk check(String id) {
        return map.get(id);
    }
}
