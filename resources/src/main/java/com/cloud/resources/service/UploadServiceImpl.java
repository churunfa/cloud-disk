package com.cloud.resources.service;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.ChunkStatus;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import com.cloud.resources.mapper.ChunkMapper;
import com.cloud.resources.mapper.FileMapper;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class UploadServiceImpl implements UploadService{

    FileMapper fileMapper;

    ChunkMapper chunkMapper;

    FileService fileService;

    UploadService uploadService;

    public BlockingQueue<Integer> queue;

    public UploadServiceImpl() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) list.add(i);
        queue = new LinkedBlockingQueue(list);
    }

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public void setChunkMapper(ChunkMapper chunkMapper) {
        this.chunkMapper = chunkMapper;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    public void setQueue(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    private FileDB insert(String md5, String name) {
        FileDB fileDB = new FileDB();
        fileDB.setFilename(name); //随便填
        fileDB.setGmt_create(new Date());
        fileDB.setGmt_modified(new Date());
        fileDB.setMd5(md5);
        String path = FileUtil.get("/test"); // --随便填
        fileDB.setPath(path);
        fileMapper.insert(fileDB);
        return fileDB;
    }

    private FileDB update(FileDB fileDB, String extension) {
        int id = fileDB.getId();
        int block = id / 100;
        FileDB secondUpdate = new FileDB();
        secondUpdate.setId(fileDB.getId());
        secondUpdate.setPath("/block-" + block);
        secondUpdate.setFilename(fileDB.getId() + extension);
        secondUpdate.setSize(fileDB.getSize());

        fileMapper.update(secondUpdate);

        String newPath = "/block-" + block + "/" + id + extension;

        fileDB.setPath(newPath);
        fileDB.setFilename(secondUpdate.getFilename());
        return fileDB;
    }

    @Transactional
    FileDB saveFile_(MultipartFile file) {
        String md5 = MD5.getMD5(file);
        String extension = FileUtil.getExtension(file.getOriginalFilename());

        FileDB fileDB = insert(md5, file.getOriginalFilename());
        int id = fileDB.getId();

        int block = id / 100;

        File dir = new File(FileUtil.get("block-" + block));
        if (!dir.exists()) dir.mkdirs();

        String newPath = "/block-" + block + "/" + id + extension;

        try {
            file.transferTo(Paths.get(FileUtil.get(newPath)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        long size = file.getSize();
        fileDB.setSize(size);
        return update(fileDB, extension);
    }

    @Override
    @Transactional
    public FileDB saveFile(MultipartFile file) {
        if (file == null) return null;

        String md5 = MD5.getMD5(file);

        String extension = FileUtil.getExtension(file.getOriginalFilename());

        List<FileDB> fileDBs = fileMapper.queryByMd5(md5);

        if (fileDBs == null || fileDBs.isEmpty()) return saveFile_(file);

        Integer id = null;
        try {
            id = queue.take();
            String cPath = FileUtil.get("/check/" + id + "/");
            String cRealPath = cPath + file.getOriginalFilename();

            File cFile = new File(cPath);
            if (!cFile.exists()) cFile.mkdirs();

            file.transferTo(new File(cRealPath));

            for (FileDB fileDB : fileDBs) {
                try {
                    String path = fileDB.getPath();
                    String realPath = FileUtil.get(path + "/" + fileDB.getFilename());

                    File tmpFile = new File(cPath + fileDB.getFilename());

                    FileUtil.copy(new FileInputStream(realPath), new FileOutputStream(tmpFile));

                    Boolean flag = FileUtil.compareFile(realPath, tmpFile.getPath());

                    FileUtil.copy(new FileInputStream(tmpFile), new FileOutputStream(realPath));

                    if (flag) System.out.println(fileDB + "相等！！");

                    if (flag) return fileDB;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }

            File file1 = new File(cRealPath);

            FileDB fileDB = insert(md5, file.getOriginalFilename());

            fileDB.setSize(file.getSize());

            int fid = fileDB.getId();
            int block = fid / 100;

            String newPath = "/block-" + block + "/" + fid + extension;

            String newRealPath = FileUtil.get(newPath);

            FileUtil.copy(new FileInputStream(file1), new FileOutputStream(newRealPath));

            return update(fileDB, extension);


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                FileUtil.refresh("check", id);
                queue.put(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    @Override
    @Transactional
    public Chunk chunkUpload(MultipartFile file, Integer user_file_id, Integer chunk_number) {
        System.out.println(file.getOriginalFilename());
        System.out.println(user_file_id);
        System.out.println(chunk_number);
        int block = user_file_id / 100;

        // chunk-0/1/1.txt
        String path_ = String.format("/chunk-%d/%d", block, user_file_id);
        String name = String.format("%d%s", chunk_number, FileUtil.getExtension(file.getOriginalFilename()));

        String path = FileUtil.get(path_);

        System.out.println(path + name);

        File chunk_file = new File(path);
        if (!chunk_file.exists()) chunk_file.mkdirs();


        Chunk queryChunk = chunkMapper.queryChunk(user_file_id, chunk_number);

        if (queryChunk == null) {
            Chunk chunk = new Chunk();
            chunk.setPath(path_);
            chunk.setSize(file.getSize());
            chunk.setName(name);
            chunk.setMd5(MD5.getMD5(file));
            chunk.setUser_file_id(user_file_id);
            chunk.setFileDB(new FileDB());
            chunk.setGmt_create(new Date());
            chunk.setGmt_modified(new Date());
            chunk.setChunk_number(chunk_number);

            chunkMapper.insert(chunk);

            try {
                file.transferTo(new File(path + "/" + name));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            Chunk updateChunk = new Chunk();

            updateChunk.setId(chunk.getId());
            updateChunk.setFileDB(new FileDB());
            updateChunk.setGmt_modified(new Date());
            updateChunk.setStatus(ChunkStatus.PENDING);

            chunkMapper.update(updateChunk);


            chunk.setGmt_modified(updateChunk.getGmt_modified());
            chunk.setStatus(ChunkStatus.PENDING);
            return chunk;

        }

        return queryChunk;
    }

    @Transactional
    @Override
    public FileDB merge_trans(Long count, Integer user_file_id, Long chunk_size, String name){
        String margePath = null;
        String path1 = null;
        String path2 = null;
        String filePath = null;

        int sum = 0;

        for (int i = 0; i < count; i++) {
            Chunk chunk = chunkMapper.queryChunk(user_file_id, i);

            if (chunk == null || chunk.getStatus() == ChunkStatus.UPLOADING) {
                System.out.println("编号为" + i + "的文件未上传完成");
                return null;
            }

            String path = FileUtil.get(chunk.getPath());

            path1 = path + "/" + chunk.getName();
            path2 = path + "/merge-" + name;

            margePath = chunk.getPath() + "/merge-" + name;

            filePath =FileUtil.get(chunk.getPath());

            if (chunk.getStatus() == ChunkStatus.FINISH) continue;

            if (chunk.getChunk_number() != count - 1 && !chunk.getSize().equals(chunk_size)) {
                System.out.println("文件块受损");
                chunkMapper.delete(chunk.getId());
                return null;
            }

            System.out.println("文件" + chunk.getUser_file_id() + "的第" + chunk.getChunk_number() + "块进行合并");
            Boolean merge = FileUtil.merge(path1, path2);
            if (!merge) {
                System.out.println("合并失败。。");
                return null;
            }

            Chunk updateChunk = new Chunk();
            updateChunk.setId(chunk.getId());
            updateChunk.setFileDB(new FileDB());
            updateChunk.setStatus(ChunkStatus.FINISH);
            updateChunk.setGmt_modified(new Date());
            chunkMapper.update(updateChunk);

            System.out.println("文件" + chunk.getUser_file_id() + "的第" + chunk.getChunk_number() + "块状态更新");

            new File(path1).delete();

            sum = i;
        }

        if (sum != count - 1) {
            System.out.println("释放锁" + user_file_id);
            return null;
        }

        System.out.println("去重逻辑。。。。");
        System.out.println(path2);
        File file = new File(path2);

        String md5 = MD5.getMD5(file);

        List<FileDB> fileDBS = fileMapper.queryByMd5(md5);

        if (fileDBS.isEmpty()){
            FileDB fileDB = fileService.moveFile(margePath);

            UserFile userFile = new UserFile();
            userFile.setId(user_file_id);
            userFile.setGmt_modified(new Date());
            userFile.setFile(fileDB);
            userFile.setDelete(false);
            userFile.setUser(new User());
            fileMapper.updateUserFile(userFile);

            File file1 = new File(filePath);
            FileUtil.deleteFile(file1);

            return fileDB;
        }

        for (FileDB fileDB : fileDBS) {
            try {
                Boolean flag = FileUtil.compareFile(FileUtil.get(fileDB.getPath() + "/" + fileDB.getFilename()), FileUtil.get(margePath));
                if (flag) {
                    UserFile userFile = new UserFile();
                    userFile.setId(user_file_id);
                    userFile.setGmt_modified(new Date());
                    userFile.setUser(new User());
                    userFile.setDelete(false);
                    userFile.setFile(fileDB);
                    fileMapper.updateUserFile(userFile);

                    File file1 = new File(filePath);
                    FileUtil.deleteFile(file1);

                    return fileDB;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        FileDB fileDB = fileService.moveFile(margePath);

        UserFile userFile = new UserFile();
        userFile.setId(user_file_id);
        userFile.setGmt_modified(new Date());
        userFile.setDelete(false);
        userFile.setUser(new User());
        userFile.setFile(fileDB);
        fileMapper.updateUserFile(userFile);

        File file1 = new File(filePath);
        FileUtil.deleteFile(file1);
        return fileDB;
    }

    @Override
    public FileDB merge(Integer user_file_id, Long chunk_size, Long tot_size, String name) {
        Long count = (tot_size + chunk_size - 1) / chunk_size;
        System.out.println("块数：" + count);

        System.out.println("获取" + user_file_id + "锁");

        synchronized (String.valueOf(user_file_id).intern()) {
            return uploadService.merge_trans(count, user_file_id, chunk_size, name);
        }
    }

}
