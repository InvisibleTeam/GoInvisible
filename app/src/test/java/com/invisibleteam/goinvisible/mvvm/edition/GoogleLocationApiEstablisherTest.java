package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class GoogleLocationApiEstablisherTest {

    private GoogleLocationApiEstablisher establisher;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        establisher = new GoogleLocationApiEstablisher(builder);
    }

    @Test
    public void whenGoogleApiIsNotBuild_ApiIsNotConnected() {
        //When
        boolean apiConnected = establisher.isApiConnected();

        //Then
        assertEquals(false, apiConnected);
    }

    @Test
    public void whenConnectionIsRequestedAndApiClientIsBuild_GoogleApiConnectIsCalled() {
        //Given
        GoogleApiClient googleApiClient = mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;

        //When
        establisher.requestConnection();

        //Then
        verify(googleApiClient).connect();
    }

    @Test
    public void whenGoogleApiIsNotConnected_ApiIsNotConnected() {
        //Given
        GoogleApiClient googleApiClient = mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;
        when(googleApiClient.isConnected()).thenReturn(false);

        //When
        boolean apiConnected = establisher.isApiConnected();

        //Then
        assertEquals(false, apiConnected);
    }

    @Test
    public void whenGoogleApiBuildAndConnected_ApiIsConnected() {
        //Given
        GoogleApiClient googleApiClient = mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;
        when(googleApiClient.isConnected()).thenReturn(true);

        //When
        boolean apiConnected = establisher.isApiConnected();

        //Then
        assertEquals(true, apiConnected);
    }

    @Test
    public void whenConnectionRequested_ApiClientBuild() {
        //When
        establisher.requestConnection();

        //Then
        assertNotNull(establisher.googleApiClient);
    }

    @Test
    public void whenOnConnectedInvoked_ListenerOnSuccessCalled() {
        //Given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //When
        connectionCallbacks.onConnected(null);

        //Then
        verify(listener).onSuccess();
    }

    @Test
    public void whenOnConnectedInvokedAndThereIsNoListener_ErrorNotThrown() {
        //Given
        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //When (//Then - if exception is thrown test will fail)
        connectionCallbacks.onConnected(null);
    }

    @Test
    public void whenOnConnectionSuspendedInvoked_ListenerNotTouched() {
        //Given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //When
        connectionCallbacks.onConnectionSuspended(0);

        //Then
        verifyZeroInteractions(listener);
    }

    @Test
    public void whenOnConnectionFailedInvoked_ListenerOnFailureCalled() {
        //Given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                establisher.buildConnectionFailedListener();

        //When
        onConnectionFailedListener.onConnectionFailed(new ConnectionResult(0));

        //Then
        verify(listener).onFailure();
    }

    @Test
    public void whenOnConnectionFailedInvokedAndThereIsNoListener_ErrorNotThrown() {
        //Given
        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                establisher.buildConnectionFailedListener();

        //When (//Then - if exception is thrown test will fail)
        onConnectionFailedListener.onConnectionFailed(new ConnectionResult(0));
    }

    @Test
    public void whenConnectionRequested_NonNullGoogleApiInstanceReturned() {
        //Given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        //When
        establisher.requestConnection();

        //Then
        assertNotNull(establisher.getGoogleApiClient());
    }
}