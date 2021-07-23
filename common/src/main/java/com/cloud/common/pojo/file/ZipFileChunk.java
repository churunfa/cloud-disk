package com.cloud.common.pojo.file;

import java.util.Map;

public class ZipFileChunk {
    private String id;
    private String zipName;
    private Integer chunkSize;
    private Integer chunkNo;
    private Long totSize;
    private Integer task;
    private Map<String, String> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(Integer chunkNo) {
        this.chunkNo = chunkNo;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public Long getTotSize() {
        return totSize;
    }

    public void setTotSize(Long totSize) {
        this.totSize = totSize;
    }

    @Override
    public String toString() {
        return "ZipFileChunk{" +
                "id=" + id +
                ", zipName='" + zipName + '\'' +
                ", chunkSize=" + chunkSize +
                ", chunkNo=" + chunkNo +
                ", totSize=" + totSize +
                ", task=" + task +
                ", files=" + files +
                '}';
    }
}
