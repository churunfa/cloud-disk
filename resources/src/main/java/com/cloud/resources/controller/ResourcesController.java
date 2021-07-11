package com.cloud.resources.controller;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.resources.service.DownloadService;
import com.cloud.resources.service.UploadService;
import com.cloud.resources.service.UploadServiceImpl;
import com.cloud.resources.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourcesController {
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

    @PostMapping("/upload")
    public Map upload(@RequestBody MultipartFile file) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", false);
        if (file == null) {
            map.put("msg", "file为空");
            return map;
        }
        map.put("msg", "文件存储失败");
        FileDB fileDB = uploadService.saveFile(file);
        System.out.println("fileDB" + fileDB);
        if (fileDB == null) return map;
        map.put("success", true);
        map.put("msg", fileDB);
        return map;
    }

    @PostMapping("/downloads")
    public ResponseEntity<byte[]> downloads(@RequestBody ZipFile zipFile) {
        return resourcesService.downloadByPaths(zipFile);
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("id") Integer id, @RequestParam("filename") String filename) {
        return resourcesService.downloadByPath(id, filename);
    }
}
