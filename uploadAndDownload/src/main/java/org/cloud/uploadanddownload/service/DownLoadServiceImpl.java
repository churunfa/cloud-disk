package org.cloud.uploadanddownload.service;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.*;
import org.cloud.uploadanddownload.controller.ResourcesService;
import org.cloud.uploadanddownload.mapper.ShareMapper;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DownLoadServiceImpl implements DownLoadService{

    UserFileMapper userFileMapper;
    ResourcesService resourcesService;
    ShareMapper shareMapper;

    @Autowired
    public void setUserFileMapper(UserFileMapper userFileMapper) {
        this.userFileMapper = userFileMapper;
    }

    @Autowired
    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Autowired
    public void setShareMapper(ShareMapper shareMapper) {
        this.shareMapper = shareMapper;
    }

    @Override
    public ResponseEntity<byte[]> download(Integer id, User user) {

        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setUser(new User());
        userFile.setFile(new FileDB());
        List<UserFile> userFiles = userFileMapper.queryByUserFile(userFile);
        if (userFiles == null || userFiles.isEmpty()||userFiles.size() != 1) return null;

        userFile = userFiles.get(0);

        if (userFile.getUser().getId() != user.getId()) return null;

        return resourcesService.download(userFile.getFile().getId(), userFile.getFile_name());
    }

    @Override
    public ResponseEntity<byte[]> downloads(List<Integer> list, User user) {

        ZipFile zipFile = new ZipFile();
        Map<String, String> map = new HashMap<>();


        for (Integer integer : list) {
            UserFile userFile = new UserFile();
            userFile.setId(integer);
            userFile.setFile(new FileDB());
            userFile.setUser(user);
            List<UserFile> userFiles = userFileMapper.queryByUserFile(userFile);
            if (userFiles == null || userFiles.isEmpty()) return null;
            String path = userFiles.get(0).getFile().getPath();
            String name = userFiles.get(0).getFile().getFilename();
            map.put(path + "/" + name, userFiles.get(0).getFile_name());
        }

        zipFile.setFiles(map);
        zipFile.setZipName(user.getUsername() + "打包下载.zip");

        System.out.println(map);

        return resourcesService.downloads(zipFile);
    }

    @Override
    public ResponseEntity<byte[]> shareDownload(int id, String password, String name) {
        Share share = shareMapper.queryShareById(id);

        System.out.println(share);

        if (share.getStatus() == Status.PASSWORD && !share.getToken().equals(password)) return null;
        if (share.getInvalid_time().getTime() < new Date().getTime()) return null;

        return resourcesService.download(share.getUserFile().getFile().getId(), name);
    }

}
