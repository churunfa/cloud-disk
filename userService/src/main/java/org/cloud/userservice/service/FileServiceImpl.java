package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.*;
import org.cloud.userservice.mapper.FileMapper;
import org.cloud.userservice.mapper.RecycleMapper;
import org.cloud.userservice.mapper.ShareMapper;
import org.cloud.userservice.mapper.UserMapper;
import org.cloud.userservice.utils.IPUtil;
import org.cloud.userservice.utils.ShareParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService{

    FileMapper fileMapper;

    UserMapper userMapper;

    ShareMapper shareMapper;

    RecycleUtilsService recycleUtilsService;

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setShareMapper(ShareMapper shareMapper) {
        this.shareMapper = shareMapper;
    }

    @Autowired
    public void setRecycleUtilsService(RecycleUtilsService recycleUtilsService) {
        this.recycleUtilsService = recycleUtilsService;
    }

    @Override
    public PageBean<UserFile> getFileList(String dir, PageBean pageBean, User user) {
        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setFile(new FileDB());
        userFile.setDir(dir);


        int count = fileMapper.queryCountByUserFile(userFile);

        if (count == 0) {
            pageBean.setPageData(new ArrayList());
            return pageBean;
        }

        pageBean.setRecordCount(count);
        pageBean.setTotalPages((count + pageBean.getPageSize() - 1)/ pageBean.getPageSize());
        pageBean.init();

        if (pageBean.getPageNo() <= 0 || pageBean.getPageNo() > pageBean.getTotalPages()) return null;


        List<UserFile> userFiles = fileMapper.queryByUserFile(
                userFile,
                (pageBean.getPageNo() - 1) * pageBean.getPageSize(),
                pageBean.getPageSize());
        pageBean.setPageData(userFiles);
        return pageBean;
    }

    @Override
    public Map newDir(String path, String name, User user) {
        HashMap<String, Object> map = new HashMap<>();

        UserFile userFile1 = new UserFile();
        userFile1.setFile(new FileDB());
        userFile1.setUser(user);
        userFile1.setDir(path);
        userFile1.setFile_name(name);
        userFile1.setDelete(false);
        int count = fileMapper.queryCountByUserFile(userFile1);
        System.out.println("创建文件夹时查询到：" + count + "条结果");

        if (count != 0) {
            map.put("success", false);
            map.put("msg", "上传失败，当前文件夹下已经存在该文件");
            return map;
        }

        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setFile(new FileDB());
        userFile.setDir(path);
        userFile.setFileType(FileType.DIR);
        userFile.setGmt_create(new Date());
        userFile.setGmt_modified(new Date());
        userFile.setFile_name(name);
        System.out.println(userFile);
        int i = fileMapper.insertUserFile(userFile);
        if (i == 0) {
            map.put("success", false);
            map.put("msg", "插入数据库失败");
            return map;
        }

        map.put("success", true);
        map.put("msg", "创建成功");

        return map;
    }

    @Override
    @Transactional
    public Map delete(Integer fileId, User user) {

        Map<String, Object> map = new HashMap<>();

        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(userFile);

        if (userFiles == null || userFiles.isEmpty()) {
            map.put("success", false);
            map.put("msg", "未找到文件");
            return map;
        }

        if (userFiles.size() != 1) {
            map.put("success", false);
            map.put("msg", "找到多个文件");
            return map;
        }

        userFile = userFiles.get(0);


        Map map1 = recycleUtilsService.addRecycle(userFile, userFile.getDir(), "/", true);
        String newName = (String) map1.get("name");

        if (userFile.getFileType() == FileType.DIR) {

            String dir = userFile.getDir() + userFile.getFile_name() + "/";
//            String newDir = userFile.getDir() + newName + "/";
            String newDir = "/" + newName + "/";


//            int count = fileMapper.updateDeleteByDir(user.getId(), dir, new Date());

            List<UserFile> userDirs = fileMapper.queryListDir(user.getId(), dir);

            int count = 0;

            for (UserFile userDir : userDirs) {
                System.out.println("删除文件中。。。");
                System.out.println(userDir);
                System.out.println(dir);
                Map map2 = recycleUtilsService.addRecycle(userDir, dir, newDir, false);
                if ((Boolean)map2.get("success")) count ++ ;
                else {
                    new RuntimeException();
                    map.put("success", false);
                    map.put("msg", "删除失败");
                    return map;
                }
            }
            System.out.println("删除" + count + "个子项目");
        }

//        UserFile updateUserFile = new UserFile();
//
//        updateUserFile.setId(userFile.getId());
//        updateUserFile.setUser(new User());
//        updateUserFile.setFile(new FileDB());
//
//        updateUserFile.setDelete(true);
//        updateUserFile.setDelete_time(new Date());
//
//        int i = fileMapper.updateUserFile(updateUserFile);


        Long userSize = userMapper.getUserSize(user.getId());
        if (userSize == null) userSize = 0L;
        userMapper.updateUserSize(userSize, user.getId());

        if ((Boolean) map1.get("success")) {
            map.put("success", true);
            map.put("msg", "删除成功");
            return map;
        }

        map.put("success", false);
        map.put("msg", "删除失败");
        return map;
    }

    @Override
    @Transactional
    public Map deletes(List<Integer> list, User user) {

        int tot = list.size();

        int count = 0;
        for (Integer integer : list) {
            Map delete = delete(integer, user);
            if ((Boolean)delete.get("success")) count++;
        }
        Map<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("msg", "共选中" + tot + "个文件/文件夹，" + "成功删除" + count + "个");

        return map;
    }

    @Override
    public Map rename(int fileId, String name, User user) {

        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(userFile);

        System.out.println(userFiles);

        Map<String, Object> map = new HashMap<>();

        if (userFiles == null || userFiles.isEmpty()) {
            map.put("success", false);
            map.put("msg", "未找到该文件");
            return map;
        }

        if (userFiles.size() != 1) {
            map.put("success", false);
            map.put("msg", "找到多个文件");
            return map;
        }

        if (name == null || name.length() == 0) {
            map.put("success", false);
            map.put("msg", "用户名为空");
            return map;
        }

        userFile = userFiles.get(0);

        System.out.println(userFile);

        UserFile userFile2 = new UserFile();
        userFile2.setUser(user);
        userFile2.setFile(new FileDB());
        userFile2.setDir(userFile.getDir());
        userFile2.setFileType(userFile.getFileType());
        userFile2.setFile_name(name);


        List<UserFile> userFiles1 = fileMapper.queryAllUserFile(userFile2);
        System.out.println(userFiles1);

        if (!userFiles1.isEmpty()) {
            map.put("success", false);
            map.put("msg", "当前目录下有其他名为" + name + "的文件或文件夹");;
            return map;
        }



        UserFile updateUserFile = new UserFile();

        updateUserFile.setId(userFile.getId());
        updateUserFile.setUser(new User());
        updateUserFile.setFile(new FileDB());
        updateUserFile.setGmt_modified(new Date());
        updateUserFile.setFile_name(name);

        System.out.println(updateUserFile);

        fileMapper.updateUserFile(updateUserFile);


        if (userFile.getFileType() == FileType.DIR) {
            String dir = userFile.getDir();
            String oldDir = dir + userFile.getFile_name() + "/"; // 旧前缀

            String newDir = dir + name + "/"; // 新前缀

            System.out.println("旧前缀：" + oldDir);
            System.out.println("新前缀：" + newDir);
            System.out.println("用户id：" + user.getId());

            fileMapper.updateDirName(oldDir, newDir, user.getId());
        }


        map.put("success", true);
        map.put("msg", "修改成功");
        return map;
    }


    private Map parse(String path) {
        Map<String, Object> map = new HashMap<>();

        if ("/".equals(path)) {
            map.put("success", true);
            return map;
        }

        int ed = path.length() - 1;
        int count = 0;
        for ( ; ed >= 0; ed--) {
            if (path.charAt(ed) == '/') count ++;
            if (count == 2) break;
        }


        if (count < 2) {
            map.put("success", false);
            return map;
        }

        String dir = path.substring(0, ed + 1);
        String name = path.substring(ed + 1, path.length() - 1);

        System.out.println("dir --> " + dir);
        System.out.println("name --> " + name);

        map.put("success", true);
        map.put("dir", dir);
        map.put("name", name);

        return map;
    }

    @Override
    public Map move(Integer id, String path, User user) {
        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(userFile);

        System.out.println("move--->ufs");
        System.out.println(userFiles);

        Map<String, Object> map = new HashMap<>();

        if (userFiles == null || userFiles.isEmpty()) {
            map.put("success", false);
            map.put("msg", "未找到该文件");
            return map;
        }

        if (userFiles.size() != 1) {
            map.put("success", false);
            map.put("msg", "找到多个文件");
            return map;
        }


        Map parse = parse(path);
        System.out.println("parse");
        System.out.println(parse);
        if (!(Boolean) parse.get("success")) {
            parse.put("msg", "路径不合法");
            return parse;
        }

        if (parse.get("dir") != null) {

            String dir = (String) parse.get("dir");
            String name = (String) parse.get("name");

            UserFile userFile1 = new UserFile();
            userFile1.setUser(new User());
            userFile1.setFile(new FileDB());
            userFile1.setDir(dir);
            userFile1.setFile_name(name);
            userFile1.setFileType(FileType.DIR);

            System.out.println(userFile1);

            List<UserFile> userFiles1 = fileMapper.queryAllUserFile(userFile1);
            if (userFiles1.isEmpty()) {
                map.put("success", false);
                map.put("msg", "路径不存在");
                return map;
            }

        }

        userFile = userFiles.get(0);

        String dir = userFile.getDir();

        UserFile updateFIle = new UserFile();
        updateFIle.setFile(new FileDB());
        updateFIle.setUser(user);
        updateFIle.setDir(path);

        updateFIle.setFile_name(userFile.getFile_name());
        updateFIle.setFileType(userFile.getFileType());
        updateFIle.setId(null);

        int count = fileMapper.queryCountByUserFile(updateFIle);

        System.out.println("cunt=" + count);

        if (count != 0) {
            map.put("success", false);
            if (updateFIle.getFileType() == FileType.FILE) map.put("msg", "目录下已存在同名文件");
            else map.put("msg", "目录下已存在同名文件夹");
            return map;
        }

        updateFIle.setFileType(null);
        updateFIle.setFile_name(null);
        updateFIle.setId(userFile.getId());

        fileMapper.updateUserFile(updateFIle);


        if (userFile.getFileType() == FileType.DIR) {
            String oldDir = dir + userFile.getFile_name() + "/";
            String newDir = path + userFile.getFile_name() + "/";
            fileMapper.updateDirName(oldDir, newDir, user.getId());
        }

        map.put("success", true);
        map.put("msg", "移动成功");

        return map;
    }

    private Map check(Integer id, String status, String password, User user) {
        Map<String, Object> map = new HashMap<>();

        if (id == null) {
            map.put("success", false);
            map.put("msg", "文件id为空");
            return map;
        }

        if (Status.PASSWORD.name().equals(status) && (password == null || password.length() == 0) ) {
            map.put("success", false);
            map.put("msg", "密码模式必须设置密码");
            return map;
        }

        System.out.println(status);
        System.out.println(Status.PASSWORD.equals(status));
        System.out.println(Status.PUBLIC.equals(status));
        System.out.println(Status.PUBLIC.name().equals(status));
        if (!Status.PASSWORD.name().equals(status) && !Status.PUBLIC.name().equals(status)) {
            map.put("success", false);
            map.put("msg", "分享类型不存在");
            return map;
        }

        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(userFile);

        if (userFiles.size() != 1) {
            map.put("success", false);
            map.put("msg", "未找到合适的文件");
            return map;
        }

        if (userFiles.get(0).getFileType() == FileType.DIR) {
            map.put("success", false);
            map.put("msg", "文件夹不可被分享");
            return map;
        }

        map.put("success", true);
        map.put("file", userFiles.get(0));
        return map;
    }

    @Override
    public Map share(Integer id, String status, String password, User user) {
        Map<String, Object> map = new HashMap<>();

        Map check = check(id, status, password, user);
        if (!(Boolean)check.get("success")) return check;

        UserFile file = (UserFile) check.get("file");
        System.out.println(file);

        Share share = new Share();
        share.setUser(user);
        share.setUserFile(file);
        share.setStatus(Enum.valueOf(Status.class, status));
        if (Status.PASSWORD.name().equals("PASSWORD")) share.setToken(password);

        Date date = new Date();
        share.setGmt_create(date);
        share.setGmt_modified(date);
        share.setInvalid_time(new Date(date.getTime() + 1000 * 60 * 60 * 24 * 15));

        System.out.println(share);

        int count = shareMapper.insert(share);

        if (count == 0) {
            map.put("success", false);
            map.put("msg", "分享失败");
            return map;
        }

        try {

            String host = IPUtil.getInterIP2();
            System.out.println(host);

            map.put("success", true);
            map.put("url", "http://" + host + ":8080/share_download/" + share.getId());
        } catch (SocketException e) {
            e.printStackTrace();

            map.put("success", false);
            map.put("msg", "连接获取失败");
        }

        return map;
    }

    @Override
    public Map shareInfo(int id) {
        Map<String, Object> map = new HashMap<>();
        Share share = new Share();
        share.setId(id);
        share.setUserFile(new UserFile());
        share.setUser(new User());
        List<Share> shares = shareMapper.queryAllShare(share);
        System.out.println(shares);

        if (shares.isEmpty()) {
            map.put("success", false);
            map.put("msg", "未找到该分享记录");
            return map;
        }
        share = shares.get(0);

        System.out.println(new Date() + "---" + new Date().getTime());
        System.out.println(share.getGmt_create() + "---" + share.getInvalid_time().getTime());

        if (new Date().getTime() > share.getInvalid_time().getTime() || share.getUserFile() == null) {
            System.out.println("资源失效");
            Share share1 = new Share();
            share1.setId(share.getId());
            share1.setUser(new User());
            share1.setStatus(Status.INVALID);
            share1.setUserFile(new UserFile());

            System.out.println(share1);
            shareMapper.updateShare(share1);

            map.put("success", false);
            map.put("msg", "资源已失效");
            return map;
        }

        map.put("success", true);
        map.put("data", ShareParse.getShareMsg(share));
        return map;
    }

    @Override
    public Map userShareInfo(User user) {

        Share share = new Share();
        share.setUser(user);
        share.setUserFile(new UserFile());
        List<Share> shares = shareMapper.queryAllShare(share);

        ArrayList<Map> maps = new ArrayList<>();

        for (Share share1 : shares) {

            if (share1.getUserFile() == null) continue;

            HashMap<String, Object> map = new HashMap<>();
            map.put("id", share1.getId());
            map.put("filename", share1.getUserFile().getFile_name());
            map.put("size", share1.getUserFile().getSize());
            map.put("status", share1.getStatus());
            map.put("gmt_modified", share1.getGmt_modified());
            map.put("invalid_time", share1.getInvalid_time());
            map.put("password", share1.getToken());

            String host = null;
            try {
                host = IPUtil.getInterIP2();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            map.put("url", "http://" + host + ":8080/share_download/" + share1.getId());;
            maps.add(map);
        }

        System.out.println(shares);

        Map<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("data", maps);
        return map;
    }

    @Override
    public Map changeShare(Integer id, Status status, String password, User user) {

        Map<String, Object> map = new HashMap<>();

        Share share = new Share();
        share.setId(id);
        share.setStatus(status);
        share.setUser(user);
        share.setUserFile(new UserFile());

        System.out.println(share);

        List<Share> shares = shareMapper.queryAllShare(share);

        if (shares.size() != 1) {
            map.put("success", false);
            map.put("msg", "意外的查询结果");
            return map;
        }


        Share share1 = new Share();

        share1.setId(shares.get(0).getId());
        share1.setUserFile(new UserFile());
        share1.setUser(new User());
        share1.setStatus(status);

        share1.setToken(password);

        System.out.println(share1);

        shareMapper.updateShare(share1);


        map.put("success", true);
        map.put("msg", "修改成功");

        return map;
    }

    @Override
    public Map deleteShare(int id, User user) {

        Map<String, Object> map = new HashMap<>();

        Share share = new Share();
        share.setId(id);
        share.setUser(user);
        share.setUserFile(new UserFile());

        List<Share> shares = shareMapper.queryAllShare(share);

        if (shares.size() != 1) {
            map.put("success", true);
            map.put("msg", "意外的查询结果");
            return map;
        }

        Share deleteShare = new Share();

        deleteShare.setId(share.getId());
        deleteShare.setUser(user);
        deleteShare.setUserFile(new UserFile());
        deleteShare.setStatus(Status.INVALID);

        shareMapper.updateShare(deleteShare);

        map.put("success", true);
        map.put("msg", "删除成功");

        return map;
    }

}
