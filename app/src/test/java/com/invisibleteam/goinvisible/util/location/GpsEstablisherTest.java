package com.invisibleteam.goinvisible.util.location;

import android.app.Activity;
import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.invisibleteam.goinvisible.util.location.GoogleLocationApiEstablisher;
import com.invisibleteam.goinvisible.util.location.GpsEstablisher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GpsEstablisherTest {

    private LocationManager locationManager;
    private GoogleLocationApiEstablisher googleLocationApiEstablisher;
    private GpsEstablisher gpsEstablisher;

    @Before
    public void setup() {
        Activity activity = Mockito.mock(Activity.class);
        locationManager = Mockito.mock(LocationManager.class);
        googleLocationApiEstablisher = Mockito.mock(GoogleLocationApiEstablisher.class);
        gpsEstablisher = spy(new GpsEstablisher(locationManager, googleLocationApiEstablisher, activity));
    }

    @Test
    public void whenGpsProviderIsEnabled_GpsIsEstablished() {
        //Given
        when(locationManager.isProviderEnabled(any())).thenReturn(true);

        //When
        boolean gpsEstablished = gpsEstablisher.isGpsEstablished();

        //Then
        assertTrue(gpsEstablished);
    }

    @Test
    public void whenLocationStatusIsSuccess_GpsEstablishedIsCalled() {
        //Given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        LocationSettingsResult result = buildLocationSettingsResult(LocationSettingsStatusCodes.SUCCESS);

        //When
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //Then
        verify(statusListener).onGpsEstablished();
    }

    @Test
    public void whenLocationStatusResolutionRequiredAndThereIsActivity_StartResolutionForResultIsCalled()
            throws IntentSender.SendIntentException {
        //Given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        Status resolutionRequiredStatus = new Status(LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
        LocationSettingsResult result = new LocationSettingsResult(resolutionRequiredStatus);

        //When
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //Then
        verify(gpsEstablisher).startResolutionForResult(resolutionRequiredStatus);
    }

    @Test
    public void whenLocationStatusIsOther_GoogleApiConnectionFailureIsCalled() throws IntentSender.SendIntentException {
        //Given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        LocationSettingsResult result = buildLocationSettingsResult(LocationSettingsStatusCodes.CANCELED);

        //When
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //Then
        verify(statusListener).onGoogleLocationApiConnectionFailure();
    }

    @Test
    public void whenApIsNotConnected_GoogleApiConnectionIsRequested() {
        //Given
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);

        //When
        gpsEstablisher.requestGpsConnection();

        //Then
        verify(googleLocationApiEstablisher).requestConnection();
    }

    @Test
    public void whenRequestedForApiConnectionResultsWithSuccess_GoogleApiConnectionIsRequested() {
        //Given
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);
        PendingResult result = mock(PendingResult.class);
        doReturn(result).when(gpsEstablisher).buildLocationSettingsResult();
        ArgumentCaptor<GoogleLocationApiEstablisher.GoogleApiConnectionListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);

        gpsEstablisher.requestGpsConnection();
        verify(googleLocationApiEstablisher).setGoogleApiConnectionListener(listenerArgumentCaptor.capture());

        //When
        listenerArgumentCaptor.getValue().onSuccess();

        //Then
        verify(gpsEstablisher).buildLocationSettingsResult();
        verify(result).setResultCallback(any());
    }

    @Test
    public void whenRequestedForApiConnectionResultsWithFailure_GoogleApiFailureIsCalled() {
        //Given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);
        PendingResult result = mock(PendingResult.class);
        doReturn(result).when(gpsEstablisher).buildLocationSettingsResult();
        ArgumentCaptor<GoogleLocationApiEstablisher.GoogleApiConnectionListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);

        gpsEstablisher.requestGpsConnection();
        verify(googleLocationApiEstablisher).setGoogleApiConnectionListener(listenerArgumentCaptor.capture());

        //When
        listenerArgumentCaptor.getValue().onFailure();

        //Then
        verify(statusListener).onGoogleLocationApiConnectionFailure();
    }

    private LocationSettingsResult buildLocationSettingsResult(int type) {
        Status status = new Status(type);
        return new LocationSettingsResult(status);
    }

}