package org.cloud.uploadanddownload.controller;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.UserFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.uploadanddownload.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    public RestResult<Integer> getFid(String filename, Long size, String dir, AuthorizationUser authorizationUser) {
        Integer fid = chunkService.getFid(filename, size, dir, authorizationUser.getUser());
        if (fid.equals(0)) return RestResultUtils.failed("当前目录下已存在同名文件");
        return RestResultUtils.success(fid);
    }

    @RequestMapping("upload")
    public RestResult<Chunk> chunkUpload(@RequestPart("file") MultipartFile file, @RequestParam("user_file_id") Integer user_file_id, AuthorizationUser authorizationUser, @RequestParam("chunkNo") Integer chunkNo){
        System.out.println(file.getOriginalFilename());
        System.out.println(file);
        Chunk chunk = chunkService.chunkUpload(file, user_file_id, authorizationUser.getUser(), chunkNo);
        return RestResultUtils.success(chunk);
    }

    @RequestMapping("marge")
    public RestResult marge(@RequestParam("user_file_id") Integer user_file_id, AuthorizationUser authorizationUser) {
        return chunkService.merge(user_file_id, authorizationUser.getUser());
    }

    @RequestMapping("getUploading")
    public RestResult<List<Map>> getUploading(AuthorizationUser authorizationUser) {
        return RestResultUtils.success(chunkService.getUploading(authorizationUser.getUser()));
    }

}
