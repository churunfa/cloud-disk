package org.cloud.uploadanddownload.service;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.cloud.uploadanddownload.controller.ResourcesService;
import org.cloud.uploadanddownload.mapper.ChunkMapper;
import org.cloud.uploadanddownload.mapper.ShareMapper;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.cloud.uploadanddownload.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ChunkServiceImpl implements ChunkService{

    UserMapper userMapper;

    UserFileMapper userFileMapper;

    ChunkMapper chunkMapper;

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
    public void setChunkMapper(ChunkMapper chunkMapper) {
        this.chunkMapper = chunkMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setShareMapper(ShareMapper shareMapper) {
        this.shareMapper = shareMapper;
    }

    @Override
    public Chunk chunkUpload_(MultipartFile file, Integer user_file_id, User user, Integer chunkNo) {
        return chunkUpload(file, user_file_id, user, chunkNo,5 * 1024 * 1024);
    }

    @Override
    public Chunk chunkUpload_(MultipartFile file, Integer user_file_id, User user, Integer chunkNo, Integer chunkSize) {
        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[chunkSize];

            BufferedInputStream bis = new BufferedInputStream(is);

            bis.skip(chunkNo * chunkSize);

            int len = bis.read(bytes);

            if (len == -1) return null;

            FileItemFactory factory = new DiskFileItemFactory(16, null);

            FileItem item = factory.createItem("file", file.getContentType(), true, String.format("%d-%s", chunkNo, file.getOriginalFilename()));

            OutputStream os = item.getOutputStream();
            os.write(bytes, 0, len);
            os.close();

            MultipartFile multipartFile = new CommonsMultipartFile(item);
            System.out.println(multipartFile.getOriginalFilename());
            System.out.println(multipartFile.getSize());

            UserFile userFile = new UserFile();
            userFile.setId(user_file_id);
            userFile.setUser(user);
            userFile.setFile(new FileDB());
            userFile.setFile_name(file.getOriginalFilename());
            userFile.setGmt_modified(new Date());
            userFile.setSize(file.getSize());

            System.out.println(file.getSize());
            System.out.println(userFile);

            userFileMapper.updateUserFile(userFile);

            System.out.println(multipartFile.getOriginalFilename());
            RestResult<Chunk> upload = resourcesService.chunkUpload(multipartFile, user_file_id, chunkNo);

            int count = 1;
            for (int i = 0; i < 100; i++) {
                if (upload.ok()) break;
                count ++;
                upload = resourcesService.chunkUpload(multipartFile, user_file_id, chunkNo);
            }

            System.out.println("尝试上传 " + count + " 次");

            System.out.println(upload);
            return upload.getData();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo) {
        return chunkUpload(file, user_file_id, user, chunkNo,5 * 1024 * 1024);
    }

    private Boolean check(User user, int user_file_id) {
        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setId(user_file_id);
        userFile.setFile(new FileDB());
        List<UserFile> userFiles = userFileMapper.queryALLByUserFile(userFile);
        return !userFiles.isEmpty();
    }

    @Override
    public Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo, Integer chunkSize) {

        if (!check(user, user_file_id)) return null;

        RestResult<Chunk> upload = resourcesService.chunkUpload(file, user_file_id, chunkNo);

        int count = 1;
        for (int i = 0; i < 200; i++) {
            if (upload.ok()) break;
            count ++;
            upload = resourcesService.chunkUpload(file, user_file_id, chunkNo);
        }

        System.out.println("尝试上传 " + count + " 次");

        System.out.println(upload);
        return upload.getData();
    }

    @Override
    public RestResult merge(Integer user_file_id, Long chunk_size, User user) {

        if (!check(user, user_file_id)) return RestResultUtils.failed("无权限对此文件进行操作");

        UserFile userFile = new UserFile();
        userFile.setId(user_file_id);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = userFileMapper.queryALLByUserFile(userFile);

        if (userFiles.size() != 1) return RestResultUtils.failed();

        userFile = userFiles.get(0);

        return resourcesService.merge(user_file_id, chunk_size, userFile.getSize(), userFile.getFile_name());
    }

    @Override
    public RestResult merge(Integer user_file_id, User user) {
        return merge(user_file_id, 5 * 1024 * 1024L, user);
    }

    @Override
    public Integer getFid(String filename, Long size, String dir, User user) {

        UserFile queryUserFile = new UserFile();
        queryUserFile.setUser(user);
        queryUserFile.setFile(new FileDB());
        queryUserFile.setFileType(FileType.FILE);
        queryUserFile.setDir(dir);
        queryUserFile.setFile_name(filename);

        System.out.println(queryUserFile);
        List<UserFile> userFiles = userFileMapper.queryByUserFile(queryUserFile);
        List<UserFile> userFiles1 = userFileMapper.queryUploadUserFIle(queryUserFile);

        if (!userFiles.isEmpty() || !userFiles1.isEmpty()) return 0;

        User user1 = userMapper.queryUserById(user.getId());
        System.out.println(user1);
        if (user1.getTotal_size() < user1.getCount_size() + size ) return -1;

        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setDelete(true);
        userFile.setFile(new FileDB());
        userFile.setGmt_create(new Date());
        userFile.setGmt_modified(new Date());
        userFile.setDir(dir);
        userFile.setSize(size);
        userFile.setFile_name(filename);
        userFileMapper.insertUserFile(userFile);

        Long l = userMapper.querySize(user.getId());
        if (l == null) l = 0L;
        userMapper.updateUserSize(l, user.getId());

        return userFile.getId();
    }

    @Override
    public List<Map> getUploading(User user) {

        List<UserFile> uploading = userFileMapper.getUploading(user.getId());

        ArrayList<Map> maps = new ArrayList<>();

        for (UserFile userFile : uploading) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uf", userFile);
            map.put("tot_size", userFile.getSize());
            Long upload_chunk_size = chunkMapper.getChunkSize(userFile.getId());
            if (upload_chunk_size == null) upload_chunk_size = 0L;
            map.put("upload_chunk_size", upload_chunk_size);
            maps.add(map);
        }
        return maps;
    }

    @Override
    public Boolean cancel(Integer user_file_id, User user) {
        if (!check(user, user_file_id)) return false;
        int count = userFileMapper.deleteUserFile(user_file_id, user.getId());
        Long l = userMapper.querySize(user.getId());
        if (l == null) l = 0L;
        userMapper.updateUserSize(l, user.getId());
        return count != 0;
    }

    @Override
    public Integer getNext(Integer user_file_id, User user) {
        if (!check(user, user_file_id)) return -1;

        UserFile userFile = new UserFile();
        userFile.setId(user_file_id);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = userFileMapper.queryALLByUserFile(userFile);
        userFile = userFiles.get(0);

        Long totSize = userFile.getSize();
        Long size = 5 * 1024 * 1024L;

        Long count = (totSize + size - 1) / size;

        Integer next = chunkMapper.getNext(user_file_id);
        if (next == null) next = 0;
        if (next >= count) return -2;

        return next;
    }

    @Override
    public void download(Integer id, User user, HttpServletResponse response) {
        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setUser(new User());
        userFile.setFile(new FileDB());
        List<UserFile> userFiles = userFileMapper.queryByUserFile(userFile);
        if (userFiles == null || userFiles.isEmpty()||userFiles.size() != 1) return;

        userFile = userFiles.get(0);

        if (userFile.getUser().getId() != user.getId()) return;

        Long totSize = userFile.getSize();
        int chunkSize = 5 * 1024 * 1024;
        Long count = (totSize + chunkSize - 1) / chunkSize;

        try (ServletOutputStream os = response.getOutputStream()) {
            String filename = URLEncoder.encode(userFile.getFile_name(),"UTF-8");
            response.addHeader("Content-Disposition","attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + totSize);
            response.setContentType("application/octet-stream");
            for (int i = 0; i < count; i++) {
                RestResult<byte[]> result = resourcesService.chunkDownload(userFile.getFile().getId(), chunkSize, i);
                if (result.getCode() == -1) return;
                os.write(result.getData(), 0, result.getCode());
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void downloads(List<Integer> list, User user, HttpServletResponse response) {
        ZipFileChunk zipFile = new ZipFileChunk();
        Map<String, String> map = new HashMap<>();


        for (Integer integer : list) {
            UserFile userFile = new UserFile();
            userFile.setId(integer);
            userFile.setFile(new FileDB());
            userFile.setUser(user);
            List<UserFile> userFiles = userFileMapper.queryByUserFile(userFile);
            if (userFiles == null || userFiles.isEmpty()) return;
            String path = userFiles.get(0).getFile().getPath();
            String name = userFiles.get(0).getFile().getFilename();
            map.put(userFiles.get(0).getFile_name(), path + "/" + name);
        }

        zipFile.setId(UUID.randomUUID().toString());
        zipFile.setFiles(map);
        zipFile.setZipName(user.getUsername() + "的打包下载.zip");
        zipFile.setChunkSize(5 * 1024 * 1024);

        String filename = null;

        try(ServletOutputStream os = response.getOutputStream();) {
            filename = URLEncoder.encode(zipFile.getZipName(),"UTF-8");
            response.addHeader("Content-Disposition","attachment;filename=" + filename);
            response.setContentType("application/octet-stream");

            resourcesService.getZip(zipFile);

            String uuid = zipFile.getId();
            while((zipFile = resourcesService.check(uuid).getData()) == null) { Thread.sleep(1000); }

            response.addHeader("Content-Length", "" + zipFile.getTotSize());

            System.out.println(zipFile);

            long count = (zipFile.getTotSize() + zipFile.getChunkSize() - 1) / zipFile.getChunkSize();

            System.out.println(count);

            for (int i = 0; i < count; i++) {
                zipFile.setChunkNo(i);
                RestResult<byte[]> downloads = resourcesService.downloads(zipFile);
                os.write(downloads.getData());
                os.flush();
            }
            resourcesService.deleteZip(zipFile.getTask());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(map);

    }

    @Override
    public void shareDownload(int id, String password, String name, HttpServletResponse response) {
        Share share = shareMapper.queryShareById(id);

        System.out.println(share);

        if (share.getStatus() == Status.PASSWORD && !share.getToken().equals(password)) return;
        if (share.getInvalid_time().getTime() < new Date().getTime()) return;

        Long totSize = share.getUserFile().getSize();
        int chunkSize = 5 * 1024 * 1024;
        Long count = (totSize + chunkSize - 1) / chunkSize;

        try (ServletOutputStream os = response.getOutputStream()) {
            String filename = URLEncoder.encode(name,"UTF-8");
            response.addHeader("Content-Disposition","attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + totSize);
            response.setContentType("application/octet-stream");
            for (int i = 0; i < count; i++) {
                RestResult<byte[]> result = resourcesService.chunkDownload(share.getUserFile().getFile().getId(), chunkSize, i);
                if (result.getCode() == -1) return;
                os.write(result.getData(), 0, result.getCode());
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RestResult<String> check(int id, String password, String filename) {
        Share share = shareMapper.queryShareById(id);

        System.out.println(share);

        if (share.getStatus() == Status.PASSWORD && !share.getToken().equals(password)) return RestResultUtils.failed("密码错误");
        if (share.getInvalid_time().getTime() < new Date().getTime()) return RestResultUtils.failed("分享已失效");
        return RestResultUtils.success();
    }
}
