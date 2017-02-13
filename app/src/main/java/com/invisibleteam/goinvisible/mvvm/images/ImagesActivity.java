package com.invisibleteam.goinvisible.mvvm.images;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends CommonActivity {

    public final static String TAGS_CHANGED_EXTRA = "tagsChangedExtra";
    private ImagesCallback imagesCallback;

    void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(TAGS_CHANGED_EXTRA)) {
                imagesCallback.prepareSnackBar(R.string.tags_changed_message);
                imagesCallback.showSnackBar();
            }
        }
    }

    @Override
    public void prepareView() {
        imagesCallback = builCallback();

        ActivityImagesBinding activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        setSupportActionBar(activityImagesBinding.mainToolbar);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                imagesCallback);
        activityImagesBinding.setViewModel(imagesViewModel);

        extractBundle();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback builCallback() {
        return new ImagesCallback(this);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback getImagesCallback() {
        return imagesCallback;
    }
}
