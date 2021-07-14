package com.cloud.common.pojo.file;

public class Recycle {
    UserFile userFile;
    String recycle_path;
    String recycle_name;

    public UserFile getUserFile() {
        return userFile;
    }

    public void setUserFile(UserFile userFile) {
        this.userFile = userFile;
    }

    public String getRecycle_path() {
        return recycle_path;
    }

    public void setRecycle_path(String recycle_path) {
        this.recycle_path = recycle_path;
    }

    public String getRecycle_name() {
        return recycle_name;
    }

    public void setRecycle_name(String recycle_name) {
        this.recycle_name = recycle_name;
    }

    @Override
    public String toString() {
        return "Recycle{" +
                "userFile=" + userFile +
                ", recycle_path='" + recycle_path + '\'' +
                ", recycle_name='" + recycle_name + '\'' +
                '}';
    }
}
