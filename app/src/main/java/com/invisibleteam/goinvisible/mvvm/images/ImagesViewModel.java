package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

class ImagesViewModel {
    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;
    private ImagesProvider imagesProvider;
    private ImagesView imagesView;

    ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                    ImagesProvider imagesProvider,
                    ImagesView imagesView) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;
        this.imagesProvider = imagesProvider;
        this.imagesView = imagesView;

        initRecyclerView();
    }

    private void initRecyclerView() {
        imagesCompoundRecyclerView.updateResults(imagesProvider.getImagesList());
        imagesCompoundRecyclerView.setOnItemClickListener(imageDetails -> imagesView.navigateToEdit(imageDetails));
    }
}
