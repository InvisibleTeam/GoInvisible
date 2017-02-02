package com.invisibleteam.goinvisible.util.binding;

import android.text.Editable;

import com.invisibleteam.goinvisible.util.ObservableString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TextViewBindingTextWatcherTest {

    @Mock
    private ObservableString observableString;

    @Test
    public void whenTextChanged_observableStringNotified() {
        //given
        TextViewBindingTextWatcher textViewBindingTextWatcher = new TextViewBindingTextWatcher(observableString);
        String testString = "test";

        //when
        textViewBindingTextWatcher.onTextChanged(testString, 0, 0, 4);

        //then
        verify(observableString).set(testString);
    }

    @Test
    public void beforeTextChanged_didNothing() {
        //given
        TextViewBindingTextWatcher textViewBindingTextWatcher = new TextViewBindingTextWatcher(observableString);
        String testString = "test";

        //when
        textViewBindingTextWatcher.beforeTextChanged(testString, 0, 0, 4);

        //then
        verify(observableString, times(0)).set(testString);
    }

    @Test
    public void afterTextChanged_didNothing() {
        //given
        TextViewBindingTextWatcher textViewBindingTextWatcher = new TextViewBindingTextWatcher(observableString);
        Editable mockedEditable = Mockito.mock(Editable.class);

        //when
        textViewBindingTextWatcher.afterTextChanged(mockedEditable);

        //then
        verify(observableString, times(0)).set(any());
    }

}