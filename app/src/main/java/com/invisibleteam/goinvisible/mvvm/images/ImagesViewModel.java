package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

import java.util.ArrayList;

class ImagesViewModel {
    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;

    ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;

        init();
    }

    private void init() {
        imagesCompoundRecyclerView.updateResults(new ArrayList<String>() {
            {
                add("test");
                add("test1");
            }
        });
    }
}
