package com.cloud.common.pojo.file;

import com.cloud.common.pojo.User;

import java.util.Date;

public class Share {
    private Integer id;
    private User user;
    private UserFile userFile;
    private Status status;
    private String token;
    private Date gmt_create; // 创建时间
    private Date gmt_modified; //最后修改时间
    private Date invalid_time;

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

    public UserFile getUserFile() {
        return userFile;
    }

    public void setUserFile(UserFile userFile) {
        this.userFile = userFile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Date getInvalid_time() {
        return invalid_time;
    }

    public void setInvalid_time(Date invalid_time) {
        this.invalid_time = invalid_time;
    }

    @Override
    public String toString() {
        return "Share{" +
                "id=" + id +
                ", user=" + user +
                ", userFile=" + userFile +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                ", invalid_time=" + invalid_time +
                '}';
    }
}
