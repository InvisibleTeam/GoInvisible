package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityEditBinding;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.model.GeolocationTag;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.mvvm.images.ImagesActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity implements EditTagCallback, EditMenuViewCallback {

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    public static final int PLACE_REQUEST_ID = 1;
    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();

    private EditActivityHelper editActivityHelper;
    private GpsEstablisher gpsEstablisher;
    private ImageDetails imageDetails;
    private EditViewModel editViewModel;
    private EditMenuViewModel editMenuViewModel;
    private Tag tag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            tag = savedInstanceState.getParcelable(TAG_MODEL);
        }
        editActivityHelper = new EditActivityHelper(this);
        gpsEstablisher = editActivityHelper.createGpsEstablisher(tag);
    }

    @Override
    protected void onStop() {
        editActivityHelper.onStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(TAG_MODEL, tag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
        if (editMenuViewModel.isInEditState()) {
            showSaveChangesMenuItem(menu);
        }

        return true;
    }

    private void showSaveChangesMenuItem(Menu menu) {
        menu.findItem(R.id.menu_item_save_changes).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                editMenuViewModel.onBackClick();
                return true;
            case R.id.menu_item_save_changes:
                editMenuViewModel.onApproveChangesClick();
                return true;
            case R.id.menu_item_clear_all:
                editMenuViewModel.onClearAllTagsClick();
                return true;
            case R.id.menu_item_share:
                shareImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareImage() {
        try {
            Intent intent = editActivityHelper.buildShareImageIntent(imageDetails, getContentResolver());
            startActivity(Intent.createChooser(intent, getString(R.string.share_intent_chooser_title)));
        } catch (FileNotFoundException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            //TODO log error in crashlytics
        }
    }

    @VisibleForTesting
    @Nullable
    EditViewModel getEditViewModel() {
        return editViewModel;
    }

    @Override
    public void prepareView() {
        ActivityEditBinding activityEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        setSupportActionBar(activityEditBinding.mainToolbar);
        if (extractBundle()) {
            prepareViewModels(activityEditBinding);
        } //TODO log exception to crashlitycs on else.
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    boolean extractBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Parcelable extra = extras.getParcelable(TAG_IMAGE_DETAILS);
            if (extra instanceof ImageDetails) {
                imageDetails = extras.getParcelable(TAG_IMAGE_DETAILS);
                return true;
            }
        }
        return false;
    }

    private void prepareViewModels(ActivityEditBinding activityEditBinding) {
        try {
            TagsManager tagsManager = new TagsManager(getExifInterface());

            editViewModel = new EditViewModel(
                    imageDetails.getName(),
                    imageDetails.getPath(),
                    activityEditBinding.editCompoundRecyclerView,
                    tagsManager,
                    this);
            activityEditBinding.setViewModel(editViewModel);

            editMenuViewModel = new EditMenuViewModel(
                    tagsManager,
                    activityEditBinding.editCompoundRecyclerView,
                    editViewModel,
                    this);
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
            //TODO log exception to crashlitycs on else.
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ExifInterface getExifInterface() throws IOException {
        return new ExifInterface(imageDetails.getPath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_REQUEST_ID && resultCode == Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            onNewPlace(place);
        } else if (
                requestCode == GpsEstablisher.GPS_REQUEST_CODE_DEFAULT
                        && resultCode == Activity.RESULT_OK) {
            startPlaceIntent();
        }
    }

    private void onNewPlace(Place place) {
        GeolocationTag geolocationTag = editActivityHelper.prepareNewPlacePositionTag(place, tag);
        editViewModel.onEditEnded(geolocationTag);
    }

    @Override
    public void onBackPressed() {
        editMenuViewModel.onBackClick();
    }

    @Override
    public void changeViewToEditMode() {
        invalidateOptionsMenu();
    }

    @Override
    public void openPlacePickerView(Tag tag) {
        this.tag = tag;
        startPlaceIntent();
    }

    @Override
    public void showUnmodifiableTagMessage() {
        showSnackBar(R.string.unmodifiable_tag_message);
    }

    @Override
    public void showTagEditionErrorMessage() {
        showSnackBar(R.string.error_message);
    }

    @Override
    public void showTagEditionView(Tag tag) {
        EditDialog dialog = EditDialog.newInstance(EditActivity.this, tag);
        dialog.setViewModel(editViewModel);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void startPlaceIntent() {
        if (gpsEstablisher.isGpsEstablished()) {
            editActivityHelper.openPlacePicker(tag);
        } else {
            gpsEstablisher.requestGpsConnection();
        }
    }

    @Override
    public void showRejectChangesDialog() {
        String positiveMessage = getString(android.R.string.yes).toUpperCase(Locale.getDefault());
        String negativeMessage = getString(android.R.string.no).toUpperCase(Locale.getDefault());
        new AlertDialog
                .Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.tag_changed_title)
                .setMessage(R.string.tag_changed_message)
                .setPositiveButton(
                        positiveMessage,
                        (dialog, which) -> editMenuViewModel.onRejectTagsChangesDialogPositive())
                .setNegativeButton(
                        negativeMessage,
                        null)
                .setCancelable(true)
                .show();
    }

    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void navigateChangedImagesScreen() {
        Intent intent = ImagesActivity.buildIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void showTagsSuccessfullyUpdatedMessage() {
        Snackbar.make(findViewById(android.R.id.content), R.string.tags_changed_message_successfully, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showTagsUpdateFailureMessage() {
        Snackbar.make(findViewById(android.R.id.content), R.string.tags_changed_message_unsuccessfully, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void changeViewToDefaultMode() {
        invalidateOptionsMenu();
    }

    private void showSnackBar(int message) {
        Snackbar.make(
                this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

}
