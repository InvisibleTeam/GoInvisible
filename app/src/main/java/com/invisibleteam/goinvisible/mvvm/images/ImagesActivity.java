package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends CommonActivity implements ImagesView {

    private Snackbar snackbar;

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(this, imageDetails);
        startActivity(intent);
    }

    @Override
    public void showUnsupportedImageInfo() {
        snackbar.show();
    }

    @VisibleForTesting
    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    @Override
    public void prepareView() {
        ActivityImagesBinding activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        setSupportActionBar(activityImagesBinding.mainToolbar);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
        activityImagesBinding.setViewModel(imagesViewModel);

        snackbar = Snackbar
                .make(findViewById(android.R.id.content),
                        R.string.unsupported_extension,
                        Snackbar.LENGTH_LONG);
    }
}
