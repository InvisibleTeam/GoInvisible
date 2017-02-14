package com.invisibleteam.goinvisible.mvvm.edition;


import android.app.Activity;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

class GpsEstablisher {
    private static final String TAG = GpsEstablisher.class.getSimpleName();

    private static final int GPS_REQUEST_CODE_DEFAULT = 1234;
    private static final long LOCATION_REQUEST_INTERVAL = 100;
    private static final long LOCATION_REQUEST_FASTEST_INTERVAL = 100;

    private final GoogleLocationApiEstablisher googleLocationApiEstablisher;
    private final LocationManager locationManager;
    private final WeakReference<Activity> weakActivityReference;
    @Nullable
    private StatusListener statusListener;

    GpsEstablisher(
            LocationManager locationManager,
            GoogleLocationApiEstablisher googleLocationApiEstablisher,
            Activity activity) {
        this.googleLocationApiEstablisher = googleLocationApiEstablisher;
        this.locationManager = locationManager;
        this.weakActivityReference = new WeakReference<>(activity);
    }

    int getGpsRequestCode() {
        return GPS_REQUEST_CODE_DEFAULT;
    }

    boolean isGpsEstablished() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    void requestGpsConnection() {
        if (!googleLocationApiEstablisher.isApiConnected()) {
            googleLocationApiEstablisher.setListener(new GoogleLocationApiEstablisher.Listener() {
                @Override
                public void onSuccess() {
                    requestGps();
                }

                @Override
                public void onFailure() {
                    if (statusListener != null) {
                        statusListener.onGoogleLocationApiConnectionFailure();
                    }
                }
            });
            googleLocationApiEstablisher.requestConnection();
        } else {
            requestGps();
        }
    }

    private void requestGps() {
        LocationRequest locationRequest = buildLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleLocationApiEstablisher.getGoogleApiClient(),
                        builder.build()
                );
        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    onLocationSuccess();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    onLocationResolutionRequired(status);
                    break;
                default:
                    onGoogleApiConnectionFailure();
                    break;
            }
        });
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void onLocationSuccess() {
        if (statusListener != null) {
            statusListener.onGpsEstablished();
        }
    }

    private void onLocationResolutionRequired(Status status) {
        try {
            if (weakActivityReference.get() != null) {
                status.startResolutionForResult(
                        weakActivityReference.get(),
                        GPS_REQUEST_CODE_DEFAULT);
            } else {
                //TODO log crashlytics info
                Log.d(TAG, "No activity when trying to request GPS.");
            }
        } catch (IntentSender.SendIntentException e) {
            //TODO log crashlytics error
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void onGoogleApiConnectionFailure() {
        if (statusListener != null) {
            statusListener.onGoogleLocationApiConnectionFailure();
        }
    }

    void setStatusListener(@Nullable StatusListener listener) {
        this.statusListener = listener;
    }

    interface StatusListener {
        void onGpsEstablished();

        void onGoogleLocationApiConnectionFailure();
    }
}
