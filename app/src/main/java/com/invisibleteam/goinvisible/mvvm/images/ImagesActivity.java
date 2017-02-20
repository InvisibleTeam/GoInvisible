package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends CommonActivity {

    private ImagesCallback imagesCallback;

    public static Intent buildIntent(Context context) {
        return new Intent(context, ImagesActivity.class);
    }

    @Override
    public void prepareView() {

        ActivityImagesBinding activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        setSupportActionBar(activityImagesBinding.mainToolbar);

        imagesCallback = buildImagesCallback(activityImagesBinding.swipeRefreshLayout);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                imagesCallback);
        activityImagesBinding.setViewModel(imagesViewModel);
        activityImagesBinding.swipeRefreshLayout.setOnRefreshListener(imagesViewModel::updateImages);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback buildImagesCallback(SwipeRefreshLayout layout) {
        return new ImagesCallback(this, layout);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback getImagesCallback() {
        return imagesCallback;
    }
}
