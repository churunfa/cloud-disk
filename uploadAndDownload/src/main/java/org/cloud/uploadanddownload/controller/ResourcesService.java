package org.cloud.uploadanddownload.controller;

import com.cloud.common.model.RestResult;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.common.pojo.file.ZipFileChunk;
import feign.Headers;
import feign.RequestLine;
import org.apache.ibatis.annotations.Param;
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

    @RequestMapping(value = "/chunk/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResult<Chunk> chunkUpload(@RequestPart("file") MultipartFile file, @RequestParam("user_file_id") Integer user_file_id, @RequestParam("chunk_number") Integer chunk_number);

    @RequestMapping("/chunk/merge")
    RestResult<FileDB> merge(@RequestParam("user_file_id") Integer user_file_id, @RequestParam("chunk_size") Long chunk_size, @RequestParam("tot_size") Long tot_size, @RequestParam("name") String name);

    @RequestMapping("/chunk/download")
    RestResult<byte[]> chunkDownload(@RequestParam("fid") int fid, @RequestParam("chunkSize") int chunkSize, @RequestParam("chunkNo") int chunkNo);

    @PostMapping("/chunk/downloads")
    RestResult<byte[]> downloads(@RequestBody ZipFileChunk zipFile);

    @RequestMapping("/chunk/getZip")
    RestResult<long[]> getZip(@RequestBody ZipFileChunk zipFile);

    @RequestMapping("/chunk/delete/zip")
    RestResult deleteZip(@RequestParam("takeId") int takeId);

    @RequestMapping("/chunk/check")
    RestResult<ZipFileChunk> check(@RequestParam("id") String id);
}
