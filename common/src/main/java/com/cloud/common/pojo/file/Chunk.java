package com.cloud.common.pojo.file;

import java.util.Date;

public class Chunk {
    Integer id;
    FileDB fileDB;
    Integer user_file_id;
    String name;
    String path;
    String md5;
    Date gmt_create;
    Date gmt_modified;
    Long size;
    ChunkStatus status;
    Integer chunk_number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FileDB getFileDB() {
        return fileDB;
    }

    public void setFileDB(FileDB fileDB) {
        this.fileDB = fileDB;
    }

    public Integer getUser_file_id() {
        return user_file_id;
    }

    public void setUser_file_id(Integer user_file_id) {
        this.user_file_id = user_file_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    public Date getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(Date gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public ChunkStatus getStatus() {
        return status;
    }

    public void setStatus(ChunkStatus status) {
        this.status = status;
    }

    public Integer getChunk_number() {
        return chunk_number;
    }

    public void setChunk_number(Integer chunk_number) {
        this.chunk_number = chunk_number;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "id=" + id +
                ", fileDB=" + fileDB +
                ", user_file_id=" + user_file_id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", md5='" + md5 + '\'' +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                ", size=" + size +
                ", status=" + status +
                ", chunk_number=" + chunk_number +
                '}';
    }
}
