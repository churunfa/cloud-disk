package org.cloud.userservice.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.UserFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.userservice.service.FileService;
import org.cloud.userservice.utils.DirUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Login
@RestController
@RequestMapping("/file")
public class FileController {

    FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

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

    @RequestMapping("/delete/{id}")
    public Map delete(@PathVariable("id") Integer fileId, AuthorizationUser authorizationUser) {
        return fileService.delete(fileId, authorizationUser.getUser());
    }

    @RequestMapping("/deletes")
    public Map deletes(@RequestBody List<Integer> list, AuthorizationUser authorizationUser) {
        System.out.println(list);
        return fileService.deletes(list, authorizationUser.getUser());
    }
}
