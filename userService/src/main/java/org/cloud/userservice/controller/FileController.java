package org.cloud.userservice.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Status;
import com.cloud.common.pojo.file.UserFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.userservice.service.FileService;
import org.cloud.userservice.utils.DirUtils;
import org.cloud.userservice.utils.ShareParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Login
    @RequestMapping("/list")
    public Map getFiles(@RequestBody Map<String, Object> map_data, AuthorizationUser authorizationUser) {

        Integer pageNo = (Integer) map_data.get("pageNo");
        Integer pageSize = (Integer) map_data.get("pageSize");
        String dir = (String) map_data.get("dir");

        System.out.println(pageNo);
        System.out.println(pageSize);
        System.out.println(dir);

        HashMap<String, Object> map = new HashMap<>();
        User user = authorizationUser.getUser();

        PageBean<UserFile> pageBean = new PageBean<>();

        pageBean.setPageNo(pageNo);
        if (pageSize != null) pageBean.setPageSize(pageSize);

        if (pageBean.getPageSize() <= 0) {
            map.put("success", false);
            map.put("msg", "页面大小必须为正整数");
            return map;

        }

        PageBean<UserFile> fileList = fileService.getFileList(dir, pageBean, user);

        if (fileList == null) {
            map.put("success", false);
            map.put("msg", "数据已全部加载");
        }

        map.put("success", true);
        map.put("data", fileList);

        return map;
    }

    @Login
    @RequestMapping("/new/dir")
    public Map newDir(@RequestBody Map<String, Object> map_data, AuthorizationUser authorizationUser) {

        String path = (String) map_data.get("path");
        String name = (String) map_data.get("name");

        Map<String, Object> map = new HashMap<>();

        if(!DirUtils.checkPath(path) || !DirUtils.checkName(name)) {
            map.put("success", false);
            map.put("msg", "路径或文件名不合法");
            return map;
        }

        User user = authorizationUser.getUser();
        return fileService.newDir(path, name, user);
    }

    @Login
    @RequestMapping("/delete/{id}")
    public Map delete(@PathVariable("id") Integer fileId, AuthorizationUser authorizationUser) {
        return fileService.delete(fileId, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("/deletes")
    public Map deletes(@RequestBody List<Integer> list, AuthorizationUser authorizationUser) {
        return fileService.deletes(list, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("/rename")
    public Map rename(@RequestBody Map data_map, AuthorizationUser authorizationUser) {
        System.out.println(data_map);
        Integer id = (Integer) data_map.get("id");
        String name = (String) data_map.get("name");

        Map<String, Object> map = new HashMap<>();
        if (!DirUtils.checkName(name)) {
            map.put("success", false);
            map.put("msg", "文件名不合法");
            return map;
        }
        return fileService.rename(id, name, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("/move")
    public Map move(@RequestBody Map data_map, AuthorizationUser authorizationUser) {
        Integer id = (Integer) data_map.get("id");
        String path = (String) data_map.get("path");

        System.out.println(id);
        System.out.println(path);

        Map<String, Object> map = new HashMap<>();
        if(!DirUtils.checkPath(path)) {
            map.put("success", false);
            map.put("msg", "路径不合法");
            return map;
        }

        return fileService.move(id, path, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("/share")
    public Map share(@RequestBody Map data_map, AuthorizationUser authorizationUser) {
        String status = (String) data_map.get("status");
        String password = (String) data_map.get("password");
        Integer id = (Integer) data_map.get("id") ;
        return fileService.share(id, status, password, authorizationUser.getUser());
    }


    @RequestMapping("/shareInfo")
    public Map shareInfo(@RequestBody Map data_map) {
        System.out.println(data_map);
        Integer id = Integer.parseInt((String) data_map.get("id"));
        return fileService.shareInfo(id);
    }

    @RequestMapping("/userShareInfo")
    public Map userShareInfo(AuthorizationUser authorizationUser) {
        return fileService.userShareInfo(authorizationUser.getUser());
    }

    @RequestMapping("/changeShareInfo")
    public Map changeShareInfo(@RequestBody Map data_map, AuthorizationUser authorizationUser) {
        String status = (String) data_map.get("status");


        String password = (String) data_map.get("password");

        Integer id = (Integer) data_map.get("id") ;
        return fileService.changeShare(id, Enum.valueOf(Status.class, status), password, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("/deleteShare/{id}")
    public Map deleteShare(@PathVariable("id") int id, AuthorizationUser authorizationUser) {
        return fileService.deleteShare(id, authorizationUser.getUser());
    }
}
