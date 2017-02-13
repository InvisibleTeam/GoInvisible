package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

class GoogleLocationApiEstablisher {

    private static final String TAG = GoogleLocationApiEstablisher.class.getSimpleName();

    private Listener listener;
    private GoogleApiClient.Builder googleApiClientBuilder;
    @Nullable
    private GoogleApiClient googleApiClient;
    private WeakReference<Context> contextWeakReference;

    GoogleLocationApiEstablisher(GoogleApiClient.Builder googleApiClientBuilder, Context context) {
        this.googleApiClientBuilder = googleApiClientBuilder;
        this.contextWeakReference = new WeakReference<>(context);
    }

    boolean isApiConnected() {
        return (googleApiClient != null && googleApiClient.isConnected());
    }

    void requestConnection() {
        if (googleApiClient == null) {
            if (buildGoogleApiClient()) {
                googleApiClient.connect();
            }
        } else {
            googleApiClient.connect();
        }
    }

    private boolean buildGoogleApiClient() {
        if (contextWeakReference.get() == null) {
            Log.i(TAG, "Null context when trying to build GoogleApiClient.");
            //TODO crashlitycs log
            return false;
        } else {
            googleApiClient = new GoogleApiClient.Builder(contextWeakReference.get())
                    .addApi(LocationServices.API)
                    .addOnConnectionFailedListener(connectionResult -> {
                        if (listener != null) {
                            listener.onFailure();
                        }
                    })
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .build();

            return true;
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    @Nullable
    GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    interface Listener {
        void onSuccess();

        void onFailure();
    }
}
