package com.invisibleteam.goinvisible.mvvm.common;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.model.GeolocationTag;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.GpsEstablisher;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;

import java.util.Locale;

public abstract class CommonEditActivity extends CommonActivity {

    public static final int PLACE_REQUEST_ID = 1;
    private EditActivityHelper editActivityHelper;
    private EditViewModel editViewModel;
    private Tag tag;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_REQUEST_ID && resultCode == android.app.Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            onNewPlace(place);
        } else if (
                requestCode == GpsEstablisher.GPS_REQUEST_CODE_DEFAULT
                        && resultCode == android.app.Activity.RESULT_OK) {
            startPlaceIntent();
        }
    }

    public void showRejectChangesDialog() {
        String positiveMessage = getString(android.R.string.yes).toUpperCase(Locale.getDefault());
        String negativeMessage = getString(android.R.string.no).toUpperCase(Locale.getDefault());
        new AlertDialog
                .Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.tag_changed_title)
                .setMessage(R.string.tag_changed_message)
                .setPositiveButton(
                        positiveMessage,
                        (dialog, which) -> onRejectTagsChangesDialogPositive())
                .setNegativeButton(
                        negativeMessage,
                        null)
                .setCancelable(true)
                .show();
    }

    protected abstract void onRejectTagsChangesDialogPositive();

    public void openPlacePickerView(Tag tag) {
        this.tag = tag;
        startPlaceIntent();
    }

    public void showUnmodifiableTagMessage() {
        showSnackBar(R.string.unmodifiable_tag_message);
    }

    public void showTagEditionView(Tag tag) {
        EditDialog dialog = EditDialog.newInstance(this, tag);
        dialog.setViewModel(editViewModel);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    }

    public void showTagEditionErrorMessage() {
        showSnackBar(R.string.error_message);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected void startPlaceIntent() {
        GpsEstablisher gpsEstablisher = editActivityHelper.createGpsEstablisher(tag);
        if (gpsEstablisher.isGpsEstablished()) {
            editActivityHelper.openPlacePicker(tag);
        } else {
            gpsEstablisher.requestGpsConnection();
        }
    }

    private void onNewPlace(Place place) {
        GeolocationTag geolocationTag = editActivityHelper.prepareNewPlacePositionTag(place, tag);
        editViewModel.onEditEnded(geolocationTag);
    }

    protected void showSnackBar(int message) {
        Snackbar.make(
                this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    protected void setEditActivityHelper(EditActivityHelper editActivityHelper) {
        this.editActivityHelper = editActivityHelper;
    }

    public void setEditViewModel(EditViewModel editViewModel) {
        this.editViewModel = editViewModel;
    }

    public void showTagsSuccessfullyUpdatedMessage() {
        Snackbar.make(findViewById(android.R.id.content), R.string.tags_changed_message_successfully, Snackbar.LENGTH_LONG)
                .show();
    }

    public void showTagsUpdateFailureMessage() {
        Snackbar.make(findViewById(android.R.id.content), R.string.tags_changed_message_unsuccessfully, Snackbar.LENGTH_LONG)
                .show();
    }
}
