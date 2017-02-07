package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.os.Bundle;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.util.TagsMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditDialogTest {

    private final Tag TAG = new Tag("key", "value", TagType.build(InputType.TEXT_STRING));
    private Activity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Activity.class).create().get();
    }

    @Test
    public void whenEditDialogNewIstanceIsCalled_ProperBundleIsCreated() {
        //When
        EditDialog dialog = EditDialog.newInstance(activity, TAG);

        //Then
        assertTrue(dialog.extractTag());
        assertThat(dialog.tag, TagsMatcher.equals(TAG));
    }

    @Test
    public void whenWrongNonEmptyBundleIsPassed_BundleExtractionIsFailure() {
        //Given
        EditDialog dialog = spy(EditDialog.newInstance(activity, TAG));
        Bundle bundle = new Bundle();
        bundle.putString("key", "value");
        when(dialog.getArguments()).thenReturn(new Bundle());

        //When
        boolean isExtractionFailure = !dialog.extractTag();

        //Then
        assertTrue(isExtractionFailure);
    }

    @Test
    public void whenWrongNullBundleIsPassed_BundleExtractionIsFailure() {
        //Given
        EditDialog dialog = spy(EditDialog.newInstance(activity, TAG));
        when(dialog.getArguments()).thenReturn(null);

        //When
        boolean isExtractionFailure = !dialog.extractTag();

        //Then
        assertTrue(isExtractionFailure);
    }

    @Test
    public void whenDialogIsInitiated_onCreateDialogIsCalled() {
        //Given
        EditDialog dialog = spy(EditDialog.newInstance(activity, TAG));

        //When
        dialog.show(activity.getFragmentManager(), EditDialog.FRAGMENT_TAG);

        //Then
        verify(dialog).onCreateDialog(any());
    }
}
