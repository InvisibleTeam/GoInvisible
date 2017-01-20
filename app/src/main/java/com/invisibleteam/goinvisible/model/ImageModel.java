package com.invisibleteam.goinvisible.model;

public class ImageModel {
    private String path;
    private String name;

    public ImageModel(String path, String name) {
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
