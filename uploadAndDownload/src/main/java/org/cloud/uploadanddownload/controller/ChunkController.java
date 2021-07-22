package org.cloud.uploadanddownload.controller;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.UserFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.uploadanddownload.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Login
@RestController
@RequestMapping("chunk")
public class ChunkController {

    ChunkService chunkService;

    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @RequestMapping("getFid")
    public RestResult<Integer> getFid(@RequestBody UserFile userFile, AuthorizationUser authorizationUser) {

        String filename = userFile.getFile_name();
        Long size = userFile.getSize();
        String dir = userFile.getDir();

        System.out.println(userFile);

        Integer fid = chunkService.getFid(filename, size, dir, authorizationUser.getUser());
        if (fid.equals(0)) return RestResultUtils.failed("当前目录下已存在同名文件");
        if (fid.equals(-1)) return RestResultUtils.failed("上传失败，可用空间不足");


        return RestResultUtils.success(fid);
    }

    @RequestMapping("upload")
    public RestResult<Chunk> chunkUpload(@RequestPart("file") MultipartFile file, @RequestParam("user_file_id") Integer user_file_id, @RequestParam("chunkNo") Integer chunkNo,  AuthorizationUser authorizationUser){
        System.out.println(file.getOriginalFilename());
        System.out.println(file);
        Chunk chunk = chunkService.chunkUpload(file, user_file_id, authorizationUser.getUser(), chunkNo);
        return RestResultUtils.success(chunk);
    }

    @RequestMapping("marge/{user_file_id}")
    public RestResult marge(@PathVariable("user_file_id") Integer user_file_id, AuthorizationUser authorizationUser) {
        return chunkService.merge(user_file_id, authorizationUser.getUser());
    }

    @RequestMapping("getUploading")
    public RestResult<List<Map>> getUploading(AuthorizationUser authorizationUser) {
        return RestResultUtils.success(chunkService.getUploading(authorizationUser.getUser()));
    }

    @RequestMapping("cancel/{user_file_id}")
    public RestResult cancel(@PathVariable("user_file_id") Integer user_file_id, AuthorizationUser authorizationUser) {
        Boolean cancel = chunkService.cancel(user_file_id, authorizationUser.getUser());
        if (cancel) return RestResultUtils.success();
        return RestResultUtils.failed("未找到该条上传记录");
    }

    @RequestMapping("next/{user_file_id}")
    public RestResult<Integer> getNext(@PathVariable("user_file_id") Integer user_file_id, AuthorizationUser authorizationUser) {
        Integer next = chunkService.getNext(user_file_id, authorizationUser.getUser());

        if (next.equals(-1)) RestResultUtils.failed("您没有权限进行此操作");
        if (next.equals(-2)) RestResultUtils.success(-2);
        return RestResultUtils.success(next);
    }

}
