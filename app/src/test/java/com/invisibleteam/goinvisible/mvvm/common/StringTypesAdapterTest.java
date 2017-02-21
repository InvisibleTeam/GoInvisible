package com.invisibleteam.goinvisible.mvvm.common;

import android.content.Context;
import android.view.View;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StringTypesAdapterTest {

    private StringTypesAdapter adapter;

    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application;

        List<String> values = new ArrayList<>();
        values.add("test");
        adapter = new StringTypesAdapter(context, values);
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings
    @Test
    public void whenNullConvertViewPassed_ViewIsCreated() {
        //When
        View view = adapter.getView(0, null, null);
        //Then
        assertNotNull(view);
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings
    @Test
    public void whenNotNullConvertViewPassed_ViewIsCreated() {
        //Given
        View convertView = mock(View.class);
        StringTypesAdapter.ViewHolder holder = Mockito.mock(StringTypesAdapter.ViewHolder.class);
        doNothing().when(holder).setValueText(anyString());
        when(convertView.getTag()).thenReturn(holder);

        //When
        View view = adapter.getView(0, convertView, null);

        //Then
        assertNotNull(view);
    }
}