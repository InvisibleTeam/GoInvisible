package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityEditBinding;
import com.invisibleteam.goinvisible.model.GeolocationTag;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.mvvm.images.ImagesActivity;
import com.invisibleteam.goinvisible.util.LatLngUtil;
import com.invisibleteam.goinvisible.util.TagUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int PLACE_REQUEST_ID = 1;
    private static final int GPS_REQUEST_ID = 2;
    private static final long LOCATION_REQUEST_INTERVAL = 100;
    private static final long LOCATION_REQUEST_FASTEST_INTERVAL = 100;
    private static final int initialMapRadius = 20000;
    private static final int APPROVE_CHANGES = 1;
    private static final int CLEAR_ALL_TAGS = 2;
    private GoogleApiClient mGoogleApiClient;
    private EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager tagsManager;

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
        menu.add(0, CLEAR_ALL_TAGS, 1, R.string.clear_all_tags).setIcon(R.drawable.ic_remove_all)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
            case CLEAR_ALL_TAGS:
                clearAllTags();
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
        } else if (requestCode == GPS_REQUEST_ID && resultCode == Activity.RESULT_OK) {
            startPlaceIntent();
        }
    }

    private void onNewPlace(Place place) {
        GeolocationTag geolocationTag = prepareNewPlacePositionTag(place);
        editViewModel.onEditEnded(geolocationTag);
    }

    @NonNull
    private GeolocationTag prepareNewPlacePositionTag(Place place) {
        GeolocationTag geolocationTag = (GeolocationTag) tag;
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;

        String newLatitude = TagUtil.parseDoubleGPSToRationalGPS(latitude);
        geolocationTag.setValue(newLatitude);
        geolocationTag.setLatitudeRef(latitude > 0.0 ? "N" : "S");

        String newLongitude = TagUtil.parseDoubleGPSToRationalGPS(longitude);
        geolocationTag.setSecondValue(newLongitude);
        geolocationTag.setLongitudeRef(longitude > 0.0 ? "E" : "W");

        return geolocationTag;
    }

    private void openEditDialog(Tag tag) {
        EditDialog dialog = EditDialog.newInstance(EditActivity.this, tag);
        dialog.setViewModel(editViewModel);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    }

    private void startPlaceIntent() {
        if (checkGpsEnabled(this)) {
            openPlacePicker();
        } else {
            createLocationRequest(locationSettingsResult -> {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        openPlacePicker();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    EditActivity.this,
                                    GPS_REQUEST_ID);
                        } catch (IntentSender.SendIntentException e) {
                            //TODO log crashlytics error
                            Log.e(TAG, e.getMessage(), e);
                        }
                        break;
                    default:
                        break;
                }
            });
        }
    }

    public boolean checkGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void createLocationRequest(ResultCallback<LocationSettingsResult> callback) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            openPlacePicker();
        } else {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                LocationRequest locationRequest = buildLocationRequest();
                                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(locationRequest);
                                builder.setAlwaysShow(true);

                                PendingResult<LocationSettingsResult> result =
                                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                                                builder.build());
                                result.setResultCallback(callback);
                            }

                            @Override
                            public void onConnectionSuspended(int i) {

                            }
                        })
                        .build();
            }
            mGoogleApiClient.connect();
        }
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void openPlacePicker() {
        try {
            GeolocationTag geolocationTag = (GeolocationTag) tag;
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            if (geolocationTag.getValue() != null) {
                LatLng center = new LatLng(
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getValue()),
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getSecondValue())
                );
                LatLngBounds bounds = LatLngUtil.generateBoundsWithZoom(center, initialMapRadius);
                intentBuilder.setLatLngBounds(bounds);
            }

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void clearAllTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getAllTags();
        editViewModel.onClear(changedTags);
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
