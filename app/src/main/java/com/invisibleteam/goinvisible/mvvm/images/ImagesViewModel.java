package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

import java.util.ArrayList;

public class ImagesViewModel {
    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;

    public ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;

        init();
    }

    private void init(){
        imagesCompoundRecyclerView.updateResults(new ArrayList<String>() {{
            add("test");
            add("test1");
        }});
    }
}
