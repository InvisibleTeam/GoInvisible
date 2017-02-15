package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        //when
        boolean apiConnected = establisher.isApiConnected();

        //then
        assertEquals(false, apiConnected);
    }

    @Test
    public void whereConnectionIsRequestedAndApiClientIsBuild_GoogleApiConnectIsCalled() {
        //given
        GoogleApiClient googleApiClient = Mockito.mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;

        //when
        establisher.requestConnection();

        //then
        verify(googleApiClient).connect();
    }

    @Test
    public void whenGoogleApiIsNotConnected_ApiIsNotConnected() {
        //given
        GoogleApiClient googleApiClient = Mockito.mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;
        when(googleApiClient.isConnected()).thenReturn(false);

        //when
        boolean apiConnected = establisher.isApiConnected();

        //then
        assertEquals(false, apiConnected);
    }

    @Test
    public void whenGoogleApiBuildAndConnected_ApiIsConnected() {
        //given
        GoogleApiClient googleApiClient = Mockito.mock(GoogleApiClient.class);
        establisher.googleApiClient = googleApiClient;
        when(googleApiClient.isConnected()).thenReturn(true);

        //when
        boolean apiConnected = establisher.isApiConnected();

        //then
        assertEquals(true, apiConnected);
    }

    @Test
    public void whenConnectionRequested_ApiClientBuild() {
        //given
        //when
        establisher.requestConnection();

        //then
        assertNotNull(establisher.googleApiClient);
    }

    @Test
    public void whenOnConnectedInvoked_ListenerOnSuccessCalled() {
        //given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                Mockito.mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //when
        connectionCallbacks.onConnected(null);

        //then
        verify(listener).onSuccess();
    }

    @Test
    public void whenOnConnectedInvokedAndThereIsNoListener_ErrorNotThrown() {
        //given
        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //when (//then - if exception is thrown test will fail)
        connectionCallbacks.onConnected(null);
    }

    @Test
    public void whenOnConnectionSuspendedInvoked_ListenerNotTouched() {
        //given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                Mockito.mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                establisher.buildConnectionCallbacks();

        //when
        connectionCallbacks.onConnectionSuspended(0);

        //then
        verifyZeroInteractions(listener);
    }

    @Test
    public void whenOnConnectionFailedInvoked_ListenerOnFailureCalled() {
        //given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                Mockito.mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                establisher.buildConnectionFailedListener();

        //when
        onConnectionFailedListener.onConnectionFailed(new ConnectionResult(0));

        //then
        verify(listener).onFailure();
    }

    @Test
    public void whenOnConnectionFailedInvokedAndThereIsNoListener_ErrorNotThrown() {
        //given
        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                establisher.buildConnectionFailedListener();

        //when (//then - if exception is thrown test will fail)
        onConnectionFailedListener.onConnectionFailed(new ConnectionResult(0));
    }

    @Test
    public void whenConnectionRequested_NonNullGoogleApiInstanceReturned() {
        //given
        GoogleLocationApiEstablisher.GoogleApiConnectionListener listener =
                Mockito.mock(GoogleLocationApiEstablisher.GoogleApiConnectionListener.class);
        establisher.setGoogleApiConnectionListener(listener);

        //when
        establisher.requestConnection();

        //then
        assertNotNull(establisher.getGoogleApiClient());
    }
}