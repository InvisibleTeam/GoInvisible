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
import com.invisibleteam.goinvisible.mvvm.edition.EditDialogInterface;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.mvvm.images.ImagesActivity;
import com.invisibleteam.goinvisible.util.location.GpsEstablisher;

import java.util.Locale;

public abstract class CommonEditActivity extends CommonActivity {

    public static final int PLACE_REQUEST_ID = 1;
    private EditActivityHelper editActivityHelper;
    private EditDialogInterface editDialogInterface;
    private Tag tag;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //This is because of nullpointer exception after killing activity by GC
        if (tag == null) {
            startImagesActivity();
            return;
        }
        if (requestCode == PLACE_REQUEST_ID && resultCode == android.app.Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            onNewPlace(place);
        } else if (
                requestCode == GpsEstablisher.GPS_REQUEST_CODE_DEFAULT
                        && resultCode == android.app.Activity.RESULT_OK) {
            startPlaceIntent();
        }
    }

    private void startImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
        dialog.setDialogInterface(editDialogInterface);
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
        editDialogInterface.onEditEnded(geolocationTag);
    }

    protected void showSnackBar(int message) {
        Snackbar.make(
                findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    protected void setEditActivityHelper(EditActivityHelper editActivityHelper) {
        this.editActivityHelper = editActivityHelper;
    }

    public void setEditDialogInterface(EditDialogInterface editDialogInterface) {
        this.editDialogInterface = editDialogInterface;
    }

    public void showTagsSuccessfullyUpdatedMessage() {
        showSnackBar(R.string.tags_changed_message_successfully);
    }

    public void showTagsUpdateFailureMessage() {
        showSnackBar(R.string.tags_changed_message_unsuccessfully);
    }
}
