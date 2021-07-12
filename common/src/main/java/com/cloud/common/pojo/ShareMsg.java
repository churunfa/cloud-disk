package com.cloud.common.pojo;

import com.cloud.common.pojo.file.Status;

import java.util.Date;

public class ShareMsg {
    private Integer id;
    private Integer user_id;
    private String user_name;
    private String file_name;
    private Long file_size;
    private Status status;
    private Date invalid_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getInvalid_time() {
        return invalid_time;
    }

    public void setInvalid_time(Date invalid_time) {
        this.invalid_time = invalid_time;
    }

    @Override
    public String toString() {
        return "ShareMsg{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_size=" + file_size +
                ", status=" + status +
                ", invalid_time=" + invalid_time +
                '}';
    }
}
