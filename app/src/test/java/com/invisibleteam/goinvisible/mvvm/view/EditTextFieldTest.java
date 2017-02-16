package com.invisibleteam.goinvisible.mvvm.view;

import android.content.Context;
import android.view.KeyEvent;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditTextFieldTest {

    private EditTextField spyEditTextField;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        EditTextField editTextField = new EditTextField(context);
        spyEditTextField = spy(editTextField);
    }

    @Test
    public void whenBackButtonIsClicked_KeyboardIsHidded() {
        //Given
        KeyEvent event = mock(KeyEvent.class);
        when(event.getAction()).thenReturn(KeyEvent.ACTION_UP);

        //When
        spyEditTextField.onKeyPreIme(KeyEvent.KEYCODE_BACK, event);

        //Then
        verify(spyEditTextField).clearFocus();
        verify(spyEditTextField, times(0)).dispatchKeyEvent(event);
    }

    @Test
    public void whenNotBackButtonIsClicked_KeyboardIsNotHidded() {
        //Given
        KeyEvent event = mock(KeyEvent.class);
        when(event.getAction()).thenReturn(KeyEvent.ACTION_DOWN);

        //When
        spyEditTextField.onKeyPreIme(KeyEvent.KEYCODE_1, event);

        //Then
        verify(spyEditTextField, times(0)).clearFocus();
        verify(spyEditTextField).dispatchKeyEvent(event);
    }
}
