package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Status;
import com.cloud.common.pojo.file.UserFile;

import java.util.List;
import java.util.Map;

public interface FileService {
    PageBean<UserFile> getFileList(String dir, PageBean pageBean, User user);
    Map newDir(String path, String name, User user);
    Map delete(Integer fileId, User user);
    Map deletes(List<Integer> list, User user);
    Map rename(int fileId, String name, User user);
    Map move(Integer id, String path, User user);
    Map share(Integer id, String status, String password, User user);
    Map shareInfo(int id);
    Map userShareInfo(User user);
    Map changeShare(Integer id, Status status, String password, User user);
    Map deleteShare(int id, User user);
}
