package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
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
    public void whenGpsRequestCodeReturned_RequestCodeIsCorrect() {
        //when
        int requestCode = gpsEstablisher.getGpsRequestCode();

        //then
        assertEquals(GpsEstablisher.GPS_REQUEST_CODE_DEFAULT, requestCode);
    }

    @Test
    public void whenGpsProviderEnabled_GpsEstablished() {
        //given
        when(locationManager.isProviderEnabled(any())).thenReturn(true);

        //when
        boolean gpsEstablished = gpsEstablisher.isGpsEstablished();

        //then
        assertTrue(gpsEstablished);
    }

    @Test
    public void whenLocationStatusSuccess_GpsEstablishedCalled() {
        //given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        Status successStatus = new Status(LocationSettingsStatusCodes.SUCCESS);
        LocationSettingsResult result = new LocationSettingsResult(successStatus);

        //when
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //then
        verify(statusListener).onGpsEstablished();
    }

    @Test
    public void whenLocationStatusResolutionRequiredAndThereIsActivity_startResolutionForResultCalled()
            throws IntentSender.SendIntentException {
        //given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        Status resultionRequiredStatus = new Status(LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
        LocationSettingsResult result = new LocationSettingsResult(resultionRequiredStatus);

        //when
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //then
        verify(gpsEstablisher).startResolutionForResult(resultionRequiredStatus);
    }

    @Test
    public void whenLocationStatusOther_GoogleApiConnectionFailureCalled() throws IntentSender.SendIntentException {
        //given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);

        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(true);

        PendingResult locationSettingsResultPendingResult =
                Mockito.mock(PendingResult.class);
        doReturn(locationSettingsResultPendingResult).when(gpsEstablisher).buildLocationSettingsResult();

        ArgumentCaptor<ResultCallback> callbackCaptor = ArgumentCaptor.forClass(ResultCallback.class);

        Status canceledStatus = new Status(LocationSettingsStatusCodes.CANCELED);
        LocationSettingsResult result = new LocationSettingsResult(canceledStatus);

        //when
        gpsEstablisher.requestGpsConnection();
        verify(locationSettingsResultPendingResult).setResultCallback(callbackCaptor.capture());
        callbackCaptor.getValue().onResult(result);

        //then
        verify(statusListener).onGoogleLocationApiConnectionFailure();
    }

    @Test
    public void whenApisNotConnected_GoogleApiConnectionIsRequested() {
        //given
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);

        //when
        gpsEstablisher.requestGpsConnection();

        //then
        verify(googleLocationApiEstablisher).requestConnection();
    }

    @Test
    public void whenRequestedForApiConnectionResultsWithSuccess_GoogleApiConnectionIsRequested() {
        //given
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);
        PendingResult result = mock(PendingResult.class);
        doReturn(result).when(gpsEstablisher).buildLocationSettingsResult();
        ArgumentCaptor<GoogleLocationApiEstablisher.GoogleApiConnectionListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);

        gpsEstablisher.requestGpsConnection();
        verify(googleLocationApiEstablisher).setGoogleApiConnectionListener(listenerArgumentCaptor.capture());

        //when
        listenerArgumentCaptor.getValue().onSuccess();

        //then
        verify(gpsEstablisher).buildLocationSettingsResult();
        verify(result).setResultCallback(any());
    }

    @Test
    public void whenRequestedForApiConnectionResultsWithFailure_GoogleApiFailureCalled() {
        //given
        GpsEstablisher.StatusListener statusListener = mock(GpsEstablisher.StatusListener.class);
        gpsEstablisher.setStatusListener(statusListener);
        when(googleLocationApiEstablisher.isApiConnected()).thenReturn(false);
        PendingResult result = mock(PendingResult.class);
        doReturn(result).when(gpsEstablisher).buildLocationSettingsResult();
        ArgumentCaptor<GoogleLocationApiEstablisher.GoogleApiConnectionListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);

        gpsEstablisher.requestGpsConnection();
        verify(googleLocationApiEstablisher).setGoogleApiConnectionListener(listenerArgumentCaptor.capture());

        //when
        listenerArgumentCaptor.getValue().onFailure();

        //then
        verify(statusListener).onGoogleLocationApiConnectionFailure();
    }

}