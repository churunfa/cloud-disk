package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.file.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
public class UploadController {

    ResourcesService resourcesService;

    @Autowired
    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @RequestMapping("/upload")
    public Map upload(@RequestPart("file") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return resourcesService.upload(file);
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("path") String path, @PathParam("filename") String filename) {
        return resourcesService.download(path, filename);
    }

    @RequestMapping("/downloads")
    public ResponseEntity<byte[]> downloads() {
        ZipFile zipFile = new ZipFile();
        return resourcesService.downloads(zipFile);
    }

}
