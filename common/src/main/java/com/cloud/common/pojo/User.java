package com.cloud.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private Boolean deleted; // 用户是否被删除
    private Boolean locked; // 用户是否锁定
    private Date gmt_create; // 创建时间
    private Date gmt_modified; //最后修改时间
    private Date last_login; // 最后登陆时间
    private Long count_size; // 使用内存
    private Long total_size; // 总内存

    public User() {
    }

    public User(Integer id, String username, String password, Boolean deleted, Boolean locked, Date gmt_create, Date gmt_modified, Date last_login, Long count_size, Long total_size) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.deleted = deleted;
        this.locked = locked;
        this.gmt_create = gmt_create;
        this.gmt_modified = gmt_modified;
        this.last_login = last_login;
        this.count_size = count_size;
        this.total_size = total_size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
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

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public Long getCount_size() {
        return count_size;
    }

    public void setCount_size(Long count_size) {
        this.count_size = count_size;
    }

    public Long getTotal_size() {
        return total_size;
    }

    public void setTotal_size(Long total_size) {
        this.total_size = total_size;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", deleted=" + deleted +
                ", locked=" + locked +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                ", last_login=" + last_login +
                ", count_size=" + count_size +
                ", total_size=" + total_size +
                '}';
    }
}
