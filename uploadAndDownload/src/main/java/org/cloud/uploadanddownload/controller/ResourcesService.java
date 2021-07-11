package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.file.ZipFile;
import org.cloud.uploadanddownload.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(value = "service-resources", configuration = {MultipartSupportConfig.class})
public interface ResourcesService {

    @PostMapping(value = "/resources/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map upload(@RequestBody MultipartFile file);

    @PostMapping(value = "/resources/downloads")
    ResponseEntity<byte[]> downloads(@RequestBody ZipFile zipFile);

    @RequestMapping("/resources/download")
    ResponseEntity<byte[]> download(@RequestParam("id") Integer id, @RequestParam("filename") String filename);
}
