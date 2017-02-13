package com.invisibleteam.goinvisible.mvvm.images;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

public class ImagesCallback implements ImagesView {

    private Snackbar snackbar;
    private Activity activity;

    public ImagesCallback(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(activity, imageDetails);
        activity.startActivity(intent);
    }

    @Override
    public void prepareSnackBar(int messageId) {
        if (snackbar == null) {
            snackbar = Snackbar
                    .make(activity.findViewById(android.R.id.content),
                            messageId,
                            Snackbar.LENGTH_LONG);
        }
        snackbar.setText(messageId);
    }

    @Override
    public void showSnackBar() {
        snackbar.show();
    }

    @VisibleForTesting
    Snackbar getSnackbar() {
        return snackbar;
    }
}
