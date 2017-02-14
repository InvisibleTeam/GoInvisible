package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
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

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra(ImagesActivity.TAGS_CHANGED_EXTRA, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public void prepareView() {
        imagesCallback = buildImagesCallback();

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

    void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(TAGS_CHANGED_EXTRA)) {
                imagesCallback.prepareSnackBar(R.string.tags_changed_message);
                imagesCallback.showSnackBar();
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback buildImagesCallback() {
        return new ImagesCallback(this);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback getImagesCallback() {
        return imagesCallback;
    }
}
