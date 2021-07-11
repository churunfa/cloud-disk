package com.cloud.common.pojo;

public class AuthorizationUser {
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AuthorizationUser{" +
                "user=" + user +
                '}';
    }
}
