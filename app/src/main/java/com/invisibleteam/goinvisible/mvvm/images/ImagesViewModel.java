package com.invisibleteam.goinvisible.mvvm.images;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import java.util.List;

public abstract class ImagesViewModel {

    private final ObservableBoolean isInformationTextVisible = new ObservableBoolean(false);
    private final ImagesCompoundRecyclerView imagesCompoundRecyclerView;
    private final ImagesProvider imagesProvider;
    private final ImagesViewCallback imagesViewCallback;

    public ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                    ImagesProvider imagesProvider,
                    ImagesViewCallback imagesViewCallback) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;
        this.imagesProvider = imagesProvider;
        this.imagesViewCallback = imagesViewCallback;

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<ImageDetails> imagesDetailsList = imagesProvider.getImagesList();
        isInformationTextVisible.set(imagesDetailsList.isEmpty());
        imagesCompoundRecyclerView.updateResults(imagesDetailsList);
        imagesCompoundRecyclerView.setOnItemClickListener(buildItemClickListener());
    }

    private ImagesItemAdapter.OnItemClickListener buildItemClickListener() {
        return new ImagesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageDetails imageDetails) {
                onItemViewClick(imageDetails);
            }

            @Override
            public void onUnsupportedItemClick() {
                imagesViewCallback.prepareSnackBar(R.string.unsupported_extension);
                imagesViewCallback.showSnackBar();
            }
        };
    }

    public void updateImages() {
        List<ImageDetails> imagesDetailsList = imagesProvider.getImagesList();
        imagesCompoundRecyclerView.updateResults(imagesDetailsList);
        imagesViewCallback.onStopRefreshingImages();
    }

    protected abstract void onItemViewClick(ImageDetails imageDetails);

    public ObservableBoolean getIsInformationTextVisible() {
        return isInformationTextVisible;
    }
}
