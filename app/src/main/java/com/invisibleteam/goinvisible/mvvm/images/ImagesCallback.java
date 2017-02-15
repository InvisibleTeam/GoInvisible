package com.invisibleteam.goinvisible.mvvm.images;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

class ImagesCallback implements ImagesView {

    private Snackbar snackbar;
    private final Activity activity;
    private final SwipeRefreshLayout refreshLayout;

    public ImagesCallback(Activity activity, SwipeRefreshLayout refreshLayout) {
        this.activity = activity;
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(activity, imageDetails);
        activity.startActivity(intent);
    }

    @Override
    public void prepareSnackBar(int messageResId) {
        if (snackbar == null) {
            snackbar = Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    messageResId,
                    Snackbar.LENGTH_LONG);
        }
        snackbar.setText(messageResId);
    }

    @Override
    public void showSnackBar() {
        snackbar.show();
    }

    @Override
    public void onStopRefreshingImages() {
        refreshLayout.setRefreshing(false);
    }

    @VisibleForTesting
    Snackbar getSnackbar() {
        return snackbar;
    }
}
