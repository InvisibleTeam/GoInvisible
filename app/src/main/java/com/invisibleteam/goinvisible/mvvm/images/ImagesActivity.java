package com.invisibleteam.goinvisible.mvvm.images;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends CommonActivity implements ImagesView {

    private Snackbar snackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void navigateToEdit(ImageView view, ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(this, imageDetails);
        final ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, view, getString(R.string.transition));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
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
