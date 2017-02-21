package com.invisibleteam.goinvisible.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ClearingTagsServiceTest {

    private Context context;
    private ClearingTagsService service;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        service = Robolectric.buildService(ClearingTagsService.class).get();
        service = spy(service);
    }

    @Test
    public void WhenClearingServiceIsNotActivated_ServiceIsStopped() {
        //When
        service.onHandleIntent(new Intent());

        //Then
        verify(service).stopSelf();
        verify(service, times(0)).performTagsClearing(any());
    }

    @Test
    public void WhenClearingServiceIsActivated_ServiceIsStarted() {
        //Given
        SharedPreferencesUtil.setClearingServiceActivation(context, true);
        when(service.getImages()).thenReturn(Collections.singletonList(new ImageDetails("path", "name")));

        //When
        service.onHandleIntent(new Intent());

        //Then
        verify(service, times(0)).stopSelf();
        verify(service).performTagsClearing(any());
    }

    @Test
    public void WhenClearingServiceIsDisabledAfterActivation_ServiceIsStopped() {
        //Given
        SharedPreferencesUtil.setClearingServiceActivation(context, true);
        SharedPreferencesUtil.setClearingServiceActivation(context, false);
        when(service.getImages()).thenReturn(Collections.singletonList(new ImageDetails("path", "name")));

        //When
        service.onHandleIntent(new Intent());

        //Then
        verify(service).stopSelf();
        verify(service, times(0)).performTagsClearing(any());
    }

    @Test
    public void ClearingTagsServiceIsNotBinded() {
        //When
        IBinder iBinder = service.onBind(new Intent());

        //Then
        assertThat(iBinder, is(nullValue()));
    }

    @Test(expected = IOException.class)
    public void WhenNotProperFilePath_IOExceptionIsThrown() throws IOException {
        //When
        ImageDetails imageDetails = new ImageDetails("path", "name");

        service.getExifInterface(imageDetails);
    }
}