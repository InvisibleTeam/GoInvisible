package com.invisibleteam.goinvisible.util.binding;

import com.invisibleteam.goinvisible.util.ObservableString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

}