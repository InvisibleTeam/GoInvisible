package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.dialog.EditDialog;
import com.invisibleteam.goinvisible.util.LatLngUtil;

import java.io.IOException;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int PLACE_REQUEST_ID = 1;
    private static final int GPS_REQUEST_ID = 2;
    private static final long LOCATION_REQUEST_INTERVAL = 100;
    private static final long LOCATION_REQUEST_FASTEST_INTERVAL = 100;
    private static int initialMapRadius = 20000;
    private GoogleApiClient mGoogleApiClient;

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
            EditCompoundRecyclerView editCompoundRecyclerView =
                    (EditCompoundRecyclerView) findViewById(R.id.edit_compound_recycler_view);

            try {
                ExifInterface exifInterface = new ExifInterface(imageDetails.getPath());
                editViewModel = new EditViewModel(
                        imageDetails.getName(),
                        imageDetails.getPath(),
                        editCompoundRecyclerView,
                        new TagsManager(exifInterface),
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
        } else if (requestCode == GPS_REQUEST_ID && resultCode == Activity.RESULT_OK) {
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
}
