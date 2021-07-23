package com.cloud.resources.controller;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.common.pojo.file.ZipFileChunk;
import com.cloud.resources.service.DownloadService;
import com.cloud.resources.service.UploadService;
import com.cloud.resources.service.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chunk")
public class ChunkController {

    DownloadService resourcesService;
    UploadService uploadService;

    @Autowired
    public void setResourcesService(DownloadService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Autowired
    public void setUploadService(UploadServiceImpl uploadService) {
        this.uploadService = uploadService;
    }

    @RequestMapping("upload")
    public RestResult<Chunk> chunkUpload(MultipartFile file, Integer user_file_id, Integer chunk_number) {
        if (file == null) return RestResultUtils.failed();
        Chunk chunk = uploadService.chunkUpload(file, user_file_id, chunk_number);
        if (chunk == null) RestResultUtils.failed("您没有权限对此文件进行操作");
        return RestResultUtils.success(chunk);
    }

    @RequestMapping("merge")
    public RestResult merge(Integer user_file_id, Long chunk_size, Long tot_size, String name) {

        new Thread(()->{
            uploadService.merge(user_file_id, chunk_size, tot_size, name);
        }).start();
        return RestResultUtils.success();
    }

    @RequestMapping("download")
    public RestResult<byte[]> chunkDownload(int fid, int chunkSize, int chunkNo) {
        return resourcesService.chunkDownload(fid, chunkSize, chunkNo);
    }

    @RequestMapping("getZip")
    public RestResult<long[]> getZip(@RequestBody ZipFileChunk zipFile) {
        new Thread(()->{ resourcesService.getZip(zipFile); }).start();
        return RestResultUtils.success();
    }

    @RequestMapping("check")
    public RestResult<ZipFileChunk> check(String id) {
        return RestResultUtils.success(resourcesService.check(id));
    }

    @RequestMapping("delete/zip")
    public RestResult deleteZip(int takeId) {
        return resourcesService.deleteZip(takeId);
    }

    @PostMapping("/downloads")
    public RestResult<byte[]> downloads(@RequestBody ZipFileChunk zipFile) {
        return resourcesService.chunkDownloadByPaths(zipFile);
    }
}
