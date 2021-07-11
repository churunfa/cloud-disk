package com.cloud.common.pojo.file;

import com.cloud.common.pojo.User;

import java.util.Date;

public class UserFile {
    private Integer id;
    private User user;
    private FileDB file;
    private String file_name;
    private String dir;
    private FileType fileType;
    private Boolean delete;
    private Date delete_time;
    private Date gmt_create; // 创建时间
    private Date gmt_modified; //最后修改时间
    private Long size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileDB getFile() {
        return file;
    }

    public void setFile(FileDB file) {
        this.file = file;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Date getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Date delete_time) {
        this.delete_time = delete_time;
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

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", user=" + user +
                ", file=" + file +
                ", file_name='" + file_name + '\'' +
                ", dir='" + dir + '\'' +
                ", fileType=" + fileType +
                ", delete=" + delete +
                ", delete_time=" + delete_time +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                ", size=" + size +
                '}';
    }
}
