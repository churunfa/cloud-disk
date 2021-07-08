package com.cloud.common.pojo.file;

import java.util.List;
import java.util.Map;

public class ZipFile {
    private String zipName;
    private Map<String, String> files;

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ZipFile{" +
                "zipName='" + zipName + '\'' +
                ", files=" + files +
                '}';
    }
}
