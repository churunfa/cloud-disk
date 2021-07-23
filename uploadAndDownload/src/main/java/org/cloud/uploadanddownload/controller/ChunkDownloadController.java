package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import org.churunfa.security.grant.auth.Login;
import org.cloud.uploadanddownload.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chunk")
public class ChunkDownloadController {

    ChunkService chunkService;

    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Login
    @RequestMapping("/download/{id}")
    public void download(@PathVariable("id") Integer id, HttpServletResponse httpServletResponse, AuthorizationUser authorizationUser) {
        User user = authorizationUser.getUser();
        System.out.println(id);
        System.out.println(user);
        chunkService.download(id, user, httpServletResponse);
    }

    @Login
    @RequestMapping("/downloads")
    public void downloads(@RequestBody List<Integer> list, AuthorizationUser authorizationUser, HttpServletResponse response) {
        User user = authorizationUser.getUser();
        chunkService.downloads(list, user, response);
    }

    @RequestMapping("/share/download")
    public void shareDownload(@RequestBody Map data_map, HttpServletResponse httpServletResponse) {
        int id = Integer.parseInt((String) data_map.get("id"));
        String password = (String) data_map.get("password");
        String filename = (String) data_map.get("filename");

        chunkService.shareDownload(id, password, filename, httpServletResponse);
    }

}
