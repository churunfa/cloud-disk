package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
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

    @RequestMapping("/share/download")
    public ResponseEntity<byte[]> shareDownload(@RequestBody Map data_map) {
        int id = Integer.parseInt((String) data_map.get("id"));
        String password = (String) data_map.get("password");
        String filename = (String) data_map.get("filename");

        return downLoadService.shareDownload(id, password, filename);
    }

}
