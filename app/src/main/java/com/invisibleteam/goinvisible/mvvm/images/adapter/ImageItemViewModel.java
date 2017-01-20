package com.invisibleteam.goinvisible.mvvm.images.adapter;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.util.ObservableString;

public class ImageItemViewModel {

    private final ObservableString name = new ObservableString("");
    private String url = "";

    public void setModel(ImageDetails image) {
        this.name.set(image.getName());
        this.url = image.getPath();
    }

    public ObservableString getName() {
        return name;
    }

    public String getImageUrl() {
        return url;
    }
}
