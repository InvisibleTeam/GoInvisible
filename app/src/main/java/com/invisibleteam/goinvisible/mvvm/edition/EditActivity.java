package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityEditBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.util.LatLngUtil;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int PLACE_REQUEST_ID = 1;
    private static final int initialMapRadius = 20000;
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

    private final EditTagListener editTagListener = (tag) -> {
        if (tag.getKey().equals(ExifInterface.TAG_GPS_LATITUDE)) {
            this.tag = tag;
            startPlaceIntent();
        } else {
            openEditDialog(tag);
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
        initializeGpsEstablisher();
    }

    private void initializeGpsEstablisher(){
        LocationManager locationManager = (LocationManager) EditActivity.this.getSystemService(LOCATION_SERVICE);
        GoogleLocationApiEstablisher googleLocationApiEstablisher =
                new GoogleLocationApiEstablisher(
                        new GoogleApiClient.Builder(this),
                        this);
        gpsEstablisher = new GpsEstablisher(locationManager, googleLocationApiEstablisher, this);
        gpsEstablisher.setListener(new GpsEstablisher.Listener() {
            @Override
            public void onGpsEstablished() {
                openPlacePicker();
            }

            @Override
            public void onGoogleLocationApiConnectionFailure() {
                Toast.makeText(EditActivity.this, R.string.google_api_connection_error, Toast.LENGTH_SHORT).show();
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        tagsManager.editTags(changedTags);
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
                ExifInterface exifInterface = new ExifInterface(imageDetails.getPath());
                tagsManager = new TagsManager(exifInterface);
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
        tag.setValue(String.valueOf(place.getLatLng().latitude));
        tag.setSecondValue(String.valueOf(place.getLatLng().longitude));
        editViewModel.onEditEnded(tag);
    }

    private void openEditDialog(Tag tag) {
        EditDialog dialog = EditDialog.newInstance(EditActivity.this, tag);
        dialog.setViewModel(editViewModel);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    }

    private void startPlaceIntent() {
        if (gpsEstablisher.isGpsEstablished()) {
            openPlacePicker();
        } else {
            gpsEstablisher.requestGpsConnection();
        }
    }

    private void openPlacePicker() {
        try {
            LatLng center = new LatLng(
                    Double.valueOf(tag.getValue()),
                    Double.valueOf(tag.getSecondValue())
            );
            LatLngBounds bounds = LatLngUtil.generateBoundsWithZoom(center, initialMapRadius);

            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(bounds);
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, PLACE_REQUEST_ID);

        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, R.string.google_services_error,
                    Toast.LENGTH_LONG).show();
        }
    }

    @VisibleForTesting
    void setEditCompoundRecyclerView(EditCompoundRecyclerView editCompoundRecyclerView) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;
    }

    @VisibleForTesting
    void setTagsManager(TagsManager tagsManager) {
        this.tagsManager = tagsManager;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateTags();
    }
}
