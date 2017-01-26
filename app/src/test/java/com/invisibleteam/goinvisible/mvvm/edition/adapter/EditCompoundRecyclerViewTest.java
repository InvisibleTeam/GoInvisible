package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ObjectType;
import com.invisibleteam.goinvisible.model.Tag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditCompoundRecyclerViewTest {

    private static final Tag TAG = new Tag("key", "value", ObjectType.STRING);
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenTagIsCleared_ClearingIsPropagated() {
        //Given
        EditCompoundRecyclerView editCompoundRecyclerView =
                new EditCompoundRecyclerView(context, mock(AttributeSet.class));
        EditItemAdapter adapter = mock(EditItemAdapter.class);
        editCompoundRecyclerView.setItemAdapter(adapter);

        //When
        editCompoundRecyclerView.updateTag(TAG);

        //Then
        verify(adapter).updateTag(TAG);
    }



}
