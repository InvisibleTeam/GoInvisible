package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.util.LifecycleBinder;

public class TagDiffMicroServiceFactory {
    private final LifecycleBinder lifecycleBinder;

    public TagDiffMicroServiceFactory(LifecycleBinder lifecycleBinder) {
        this.lifecycleBinder = lifecycleBinder;
    }

    public TagDiffMicroService buildService(TagDiffMicroService.EditListener editListener) {
        return new TagDiffMicroService(lifecycleBinder, editListener);
    }
}
