package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.ZipFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.uploadanddownload.service.DownLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DownLoadController {

    DownLoadService downLoadService;

    @Autowired
    public void setDownLoadService(DownLoadService downLoadService) {
        this.downLoadService = downLoadService;
    }

    @Login
    @RequestMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") Integer id, AuthorizationUser authorizationUser) {
        User user = authorizationUser.getUser();
        System.out.println(id);
        System.out.println(user);
        return downLoadService.download(id, user);
    }

    @Login
    @RequestMapping("/downloads")
    public ResponseEntity<byte[]> downloads(@RequestBody List<Integer> list, AuthorizationUser authorizationUser) {
        User user = authorizationUser.getUser();
        return downLoadService.downloads(list, user);
    }
}
