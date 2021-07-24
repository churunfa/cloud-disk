package org.cloud.uploadanddownload.controller;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import org.cloud.uploadanddownload.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("chunk")
public class ChunkDownloadController {

    ChunkService chunkService;

    SecurityService securityService;

    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping("/download/{id}")
    public void download(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse httpServletResponse, AuthorizationUser authorizationUser) {
        Cookie[] cookies = request.getCookies();

        String jwt_token = null;

        if (cookies == null) return;

        for (Cookie cookie : cookies) if ("token".equals(cookie.getName())) jwt_token = cookie.getValue();

        if (jwt_token == null) return;

        User user = securityService.getAllInfo(jwt_token);
        if (user == null) return;

        chunkService.download(id, user, httpServletResponse);
    }

    @RequestMapping("/downloads")
    public void downloads(@RequestParam List<Integer> files, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return;

        String jwt_token = null;

        for (Cookie cookie : cookies) if ("token".equals(cookie.getName())) jwt_token = cookie.getValue();

        if (jwt_token == null) return;

        User user = securityService.getAllInfo(jwt_token);
        if (user == null) return;
        chunkService.downloads(files, user, response);
    }

    @RequestMapping("/share/download/{id}/{filename}")
    public void shareDownload(@PathVariable("id") int id, @PathVariable("filename") String filename ,@RequestParam("password") String password , HttpServletResponse httpServletResponse) {
        chunkService.shareDownload(id, password, filename, httpServletResponse);
    }
    @RequestMapping("/share/check/{id}/{filename}")
    public RestResult<String> check(@PathVariable("id") int id, @PathVariable("filename") String filename ,@RequestParam("password") String password) {
        System.out.println(password);
        return chunkService.check(id, password, filename);
    }

}
