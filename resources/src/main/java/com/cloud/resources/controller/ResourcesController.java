package com.cloud.resources.controller;

import com.cloud.common.pojo.file.ZipFile;
import com.cloud.resources.service.DownloadService;
import com.cloud.resources.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourcesController {
    DownloadService resourcesService;

    @Autowired
    public void setResourcesService(DownloadService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @PostMapping("/upload")
    public Map upload(@RequestPart("file") MultipartFile file) {
        System.out.println(file);
        if (file != null) System.out.println(file.getOriginalFilename());
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @PostMapping("/downloads")
    public ResponseEntity<byte[]> downloads(@RequestBody ZipFile zipFile) {
        return resourcesService.downloadByPaths(zipFile);
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("path") String path, @RequestParam("filename") String filename) {
        path = FileUtil.get(path);
        return resourcesService.downloadByPath(path, filename);
    }
}
