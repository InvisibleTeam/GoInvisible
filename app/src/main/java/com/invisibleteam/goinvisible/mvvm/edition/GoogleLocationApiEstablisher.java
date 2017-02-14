package com.invisibleteam.goinvisible.mvvm.edition;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.annotation.Nullable;

class GoogleLocationApiEstablisher {
    private GoogleApiConnectionListener googleApiConnectionListener;
    private GoogleApiClient.Builder googleApiClientBuilder;
    @Nullable
    private GoogleApiClient googleApiClient;

    GoogleLocationApiEstablisher(GoogleApiClient.Builder googleApiClientBuilder) {
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
                .addOnConnectionFailedListener(connectionResult -> {
                    if (googleApiConnectionListener != null) {
                        googleApiConnectionListener.onFailure();
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (googleApiConnectionListener != null) {
                            googleApiConnectionListener.onSuccess();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
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
