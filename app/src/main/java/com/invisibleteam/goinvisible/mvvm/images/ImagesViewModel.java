package com.invisibleteam.goinvisible.mvvm.images;


import android.databinding.ObservableBoolean;
import android.widget.ImageView;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import java.util.List;

public class ImagesViewModel {

    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;
    private ImagesProvider imagesProvider;
    private ImagesView imagesView;
    public ObservableBoolean isInformationTextVisible = new ObservableBoolean(false);

    ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                    ImagesProvider imagesProvider,
                    ImagesView imagesView) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;
        this.imagesProvider = imagesProvider;
        this.imagesView = imagesView;

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<ImageDetails> imagesDetailsList = imagesProvider.getImagesList();
        isInformationTextVisible.set(imagesDetailsList.isEmpty());
        imagesCompoundRecyclerView.updateResults(imagesDetailsList);
        imagesCompoundRecyclerView.setOnItemClickListener(new ImagesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageView view, ImageDetails imageDetails) {
                imagesView.navigateToEdit(view, imageDetails);
            }

            @Override
            public void onUnsupportedItemClick() {
                imagesView.showUnsupportedImageInfo();
            }
        });
    }
}
