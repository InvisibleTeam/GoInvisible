package com.invisibleteam.goinvisible.mvvm.edition;

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
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected GoogleApiClient googleApiClient;

    public GoogleLocationApiEstablisher(GoogleApiClient.Builder googleApiClientBuilder) {
        this.googleApiClientBuilder = googleApiClientBuilder;
    }

    boolean isApiConnected() {
        return (googleApiClient != null && googleApiClient.isConnected());
    }

    void requestConnection() {
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
    protected GoogleApiClient.OnConnectionFailedListener buildConnectionFailedListener() {
        return connectionResult -> {
            if (googleApiConnectionListener != null) {
                googleApiConnectionListener.onFailure();
            }
        };
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected GoogleApiClient.ConnectionCallbacks buildConnectionCallbacks() {
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

    void setGoogleApiConnectionListener(GoogleApiConnectionListener listener) {
        this.googleApiConnectionListener = listener;
    }

    @Nullable
    GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    interface GoogleApiConnectionListener {
        void onSuccess();

        void onFailure();
    }
}
