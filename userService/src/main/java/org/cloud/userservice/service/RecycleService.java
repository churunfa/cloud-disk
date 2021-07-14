package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.UserFile;

import java.util.List;
import java.util.Map;

public interface RecycleService {
    PageBean<UserFile> getFileList(String dir, PageBean<UserFile> pageBean, User user);

    Map delete(int id, User user);

    Map deletes(List<Integer> list, User user);

    Map recovery(int id, User user);

    Map recoveries(List<Integer> list, User user);
}
