package com.invisibleteam.goinvisible.util.location;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.annotation.Nullable;

public class GoogleLocationApiEstablisher {

    private GoogleApiClient.Builder googleApiClientBuilder;
    @Nullable
    private GoogleApiConnectionListener googleApiConnectionListener;
    @Nullable
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) GoogleApiClient googleApiClient;

    public GoogleLocationApiEstablisher(GoogleApiClient.Builder googleApiClientBuilder) {
        this.googleApiClientBuilder = googleApiClientBuilder;
    }

    public boolean isApiConnected() {
        return (googleApiClient != null && googleApiClient.isConnected());
    }

    public void requestConnection() {
        if (googleApiClient == null) {
            buildGoogleApiClient();
        }
        googleApiClient.connect();
    }

    private void buildGoogleApiClient() {
        googleApiClient = googleApiClientBuilder
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(buildConnectionFailedListener())
                .addConnectionCallbacks(buildConnectionCallbacks())
                .build();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    GoogleApiClient.OnConnectionFailedListener buildConnectionFailedListener() {
        return connectionResult -> {
            if (googleApiConnectionListener != null) {
                googleApiConnectionListener.onFailure();
            }
        };
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    GoogleApiClient.ConnectionCallbacks buildConnectionCallbacks() {
        return new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (googleApiConnectionListener != null) {
                    googleApiConnectionListener.onSuccess();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        };
    }

    public void setGoogleApiConnectionListener(GoogleApiConnectionListener listener) {
        this.googleApiConnectionListener = listener;
    }

    @Nullable
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public interface GoogleApiConnectionListener {
        void onSuccess();

        void onFailure();
    }
}
