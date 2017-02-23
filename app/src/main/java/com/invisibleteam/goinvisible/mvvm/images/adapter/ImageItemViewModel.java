package com.invisibleteam.goinvisible.mvvm.images.adapter;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.util.ObservableString;

public class ImageItemViewModel {

    private ObservableString name = new ObservableString("");
    private ObservableString url = new ObservableString("");

    void setModel(ImageDetails image) {
        this.name.set(image.getName());
        this.url.set(image.getPath());
    }

    public ObservableString getName() {
        return name;
    }

    public ObservableString getImageUrl() {
        return url;
    }
}
