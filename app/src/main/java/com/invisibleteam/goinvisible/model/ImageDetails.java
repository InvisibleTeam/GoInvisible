package com.invisibleteam.goinvisible.model;

public class ImageDetails {
    private String path;
    private String name;

    public ImageDetails(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
