package com.anser.model;

import java.io.Serializable;

/**
 * 用来传输的file model
 *
 * @author lht
 * @time 2018年1月14日 下午5:38:29
 */
public class FileModel implements Serializable {
    private static final long serialVersionUID = 125L;

    private String name;
    private String path;
    private boolean dir;
    private long lastModified;
    private long length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
