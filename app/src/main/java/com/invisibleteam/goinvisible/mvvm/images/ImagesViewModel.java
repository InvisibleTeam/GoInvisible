package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

public class ImagesViewModel {

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
        imagesCompoundRecyclerView.setOnItemClickListener(new ImagesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageDetails imageDetails) {
                imagesView.navigateToEdit(imageDetails);
            }

            @Override
            public void onUnsupportedItemClick() {
                imagesView.showUnsupportedImageInfo();
            }
        });
    }
}
