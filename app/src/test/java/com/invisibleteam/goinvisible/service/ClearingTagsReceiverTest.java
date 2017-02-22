package com.invisibleteam.goinvisible.service;

import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ClearingTagsReceiverTest {

    private Context context;
    private ClearingTagsReceiver receiver;

    @Before
    public void setUp() {
        context = mock(Context.class);
        receiver = new ClearingTagsReceiver();
    }

    @Test
    public void WhenProperIntentIsReceived_ClearingTagsServiceIsStarted() {
        Intent intent = new Intent();
        intent.putExtra(
                ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE_KEY,
                ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE);

        receiver.onReceive(context, intent);

        verify(context).startService(any());
    }

    @Test
    public void WhenIntentWithWrongRequestCodeIsReceived_CLearingTagsServiceIsNotStarted() {
        Intent intent = new Intent();
        intent.putExtra(ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE_KEY, 1);

        receiver.onReceive(context, intent);

        verify(context, times(0)).startService(any());
    }

    @Test
    public void WhenNotProperIntentIsReceived_CLearingTagsServiceIsNotStarted() {
        Intent intent = new Intent();

        receiver.onReceive(context, intent);

        verify(context, times(0)).startService(any());
    }
}