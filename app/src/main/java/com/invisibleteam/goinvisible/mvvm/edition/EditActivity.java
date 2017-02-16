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
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.mvvm.images.ImagesActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity {

    public static final int PLACE_REQUEST_ID = 1;
    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int APPROVE_CHANGES = 1;
    private EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager tagsManager;
    private GpsEstablisher gpsEstablisher;

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private ImageDetails imageDetails;
    private EditViewModel editViewModel;
    private Tag tag;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    final EditTagListener editTagListener = new EditTagListener() {
        @Override
        public void openEditDialog(Tag tag) {
            if (tag.getKey().equals(ExifInterface.TAG_GPS_LATITUDE)) {
                EditActivity.this.tag = tag;
                startPlaceIntent();
            } else {
                EditActivity.this.openEditDialog(tag);
            }
        }

        @Override
        public void onTagsChanged() {
            invalidateOptionsMenu();
        }
    };

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
        prepareLocationHandling();
    }

    private void prepareLocationHandling() {
        Snackbar googleApiFailureSnackbar = EditActivityHelper.createGpsSnackBar(this);
        GpsEstablisher.StatusListener gpsStatusListener = new GpsEstablisher.StatusListener() {
            @Override
            public void onGpsEstablished() {
                EditActivityHelper.openPlacePicker(tag, EditActivity.this);
            }

            @Override
            public void onGoogleLocationApiConnectionFailure() {
                googleApiFailureSnackbar.show();
            }
        };
        gpsEstablisher = EditActivityHelper.createGpsEstablisher(this, gpsStatusListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(TAG_MODEL, tag);
        super.onSaveInstanceState(outState);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editCompoundRecyclerView != null
                && !editCompoundRecyclerView.getChangedTags().isEmpty()) {
            menu.add(0, APPROVE_CHANGES, 0, R.string.save).setIcon(R.drawable.ic_approve)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case APPROVE_CHANGES:
                saveTags();
                startImagesActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startImagesActivity() {
        Intent intent = ImagesActivity.buildIntent(this);
        startActivity(intent);
        finish();
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
            editCompoundRecyclerView =
                    (EditCompoundRecyclerView) findViewById(R.id.edit_compound_recycler_view);

            try {
                tagsManager = new TagsManager(getExifInterface());
                editViewModel = new EditViewModel(
                        imageDetails.getName(),
                        imageDetails.getPath(),
                        editCompoundRecyclerView,
                        tagsManager,
                        editTagListener);
                activityEditBinding.setViewModel(editViewModel);
            } catch (IOException e) {
                Log.d(TAG, String.valueOf(e.getMessage()));
                //TODO log exception to crashlitycs on else.
            }
        } //TODO log exception to crashlitycs on else.
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
                requestCode == gpsEstablisher.getGpsRequestCode()
                        && resultCode == Activity.RESULT_OK) {
            startPlaceIntent();
        }
    }

    private void onNewPlace(Place place) {
        GeolocationTag geolocationTag = EditActivityHelper.prepareNewPlacePositionTag(place, tag);
        editViewModel.onEditEnded(geolocationTag);
    }

    private void openEditDialog(Tag tag) {
        EditDialog dialog = EditDialog.newInstance(EditActivity.this, tag);
        dialog.setViewModel(editViewModel);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    }

    private void startPlaceIntent() {
        if (gpsEstablisher.isGpsEstablished()) {
            EditActivityHelper.openPlacePicker(tag, this);
        } else {
            gpsEstablisher.requestGpsConnection();
        }
    }

    @VisibleForTesting
    void setEditCompoundRecyclerView(EditCompoundRecyclerView editCompoundRecyclerView) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;
    }

    @Override
    public void onBackPressed() {
        if (areTagsChanged()) {
            showApproveChangeTagsDialog();
            return;
        }
        super.onBackPressed();
    }

    private void saveTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        tagsManager.editTags(changedTags);
    }

    private boolean areTagsChanged() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return !changedTags.isEmpty();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void showApproveChangeTagsDialog() {
        new AlertDialog
                .Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.tag_changed_title)
                .setMessage(R.string.tag_changed_message)
                .setPositiveButton(
                        getString(android.R.string.yes).toUpperCase(Locale.getDefault()),
                        (dialog, which) -> {
                            finish();
                        })
                .setNegativeButton(
                        getString(android.R.string.no).toUpperCase(Locale.getDefault()),
                        null)
                .setCancelable(true)
                .show();
    }
}
