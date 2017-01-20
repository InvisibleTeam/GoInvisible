package com.invisibleteam.goinvisible.mvvm.images.adapter;

import com.invisibleteam.goinvisible.util.ObservableString;

public class ImageItemViewModel {

    private final ObservableString name = new ObservableString("");

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableString getName() {
        return name;
    }
}
