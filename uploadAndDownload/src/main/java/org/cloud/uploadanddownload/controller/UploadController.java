package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.ZipFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.uploadanddownload.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    UserFileService userFileService;

    @Autowired
    public void setUserFileService(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @RequestMapping("/upload")
    @Login
    public Map upload(@RequestPart("file") MultipartFile file,@RequestParam("dir") String dir, AuthorizationUser authorizationUser) throws IOException {
        User user = authorizationUser.getUser();
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            map.put("success", true);
            map.put("msg", "获取用户信息失败");
            return map;
        }

        return userFileService.upload(file, user, dir);
    }

}
