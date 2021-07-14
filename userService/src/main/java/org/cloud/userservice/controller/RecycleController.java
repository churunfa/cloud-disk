package org.cloud.userservice.controller;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.UserFile;
import org.churunfa.security.grant.auth.Login;
import org.cloud.userservice.service.RecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("recycle")
public class RecycleController {

    RecycleService recycleService;

    @Autowired
    public void setRecycleService(RecycleService recycleService) {
        this.recycleService = recycleService;
    }

    @Login
    @RequestMapping("list")
    public Map list(@RequestBody Map<String, Object> map_data, AuthorizationUser authorizationUser) {

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

        PageBean<UserFile> fileList = recycleService.getFileList(dir, pageBean, user);

        if (fileList == null) {
            map.put("success", false);
            map.put("msg", "数据已全部加载");
        }

        map.put("success", true);
        map.put("data", fileList);

        return map;
    }


    @Login
    @RequestMapping("delete/{id}")
    public Map delete(@PathVariable("id") int id, AuthorizationUser authorizationUser) {
        System.out.println(authorizationUser);
        return recycleService.delete(id, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("deletes")
    public Map deletes(@RequestBody List<Integer> list, AuthorizationUser authorizationUser) {
        return recycleService.deletes(list, authorizationUser.getUser());
    }

    @Login
    @RequestMapping("recovery/{id}")
    public Map recovery(@PathVariable("id") int id, AuthorizationUser authorizationUser) {
        return recycleService.recovery(id, authorizationUser.getUser());
    }


    @Login
    @RequestMapping("recoveries")
    public Map recoveries(@RequestBody List<Integer> list, AuthorizationUser authorizationUser) {
        return recycleService.recoveries(list, authorizationUser.getUser());
    }
}
