package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.model.InputType.DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.InputType.DATE_STRING;
import static com.invisibleteam.goinvisible.model.InputType.INDEFINITE;
import static com.invisibleteam.goinvisible.model.InputType.TEXT_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TIMESTAMP_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class DialogFactoryTest {

    private Activity activity;
    private DialogFragment dialog;
    private DialogFactory dialogFactory;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Activity.class).create().get();
        dialog = spy(DialogFragment.class);
        when(dialog.getActivity()).thenReturn(activity);
        dialogFactory = spy(new DialogFactory());
    }

    @Test
    public void whenTagWithTextStringInputTypeIsPassed_CreateTextDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(TEXT_STRING);
        Tag tag = new Tag("key", "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createTextDialog(eq(activity), eq(dialog), eq(tag), any(OnTagActionListener.class));
    }

    @Test
    public void whenTagWithTimestampStringInputTypeIsPassed_CreateTimeDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(TIMESTAMP_STRING);
        Tag tag = new Tag("key", "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createTimeDialog();
    }

    @Test
    public void whenTagWithDateStringInputTypeIsPassed_CreateDateDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(DATE_STRING);
        Tag tag = new Tag("key", "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createDateDialog();
    }

    @Test
    public void whenTagWithDatetimeStringInputTypeIsPassed_CreateDateTimeDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(DATETIME_STRING);
        Tag tag = new Tag("key", "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createDateTimeDialog();
    }

    @Test
    public void whenNullTagIsPassed_CreateErrorDialogIsCalled() {
        //When
        dialogFactory.createDialog(dialog, null, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createErrorDialog(activity);
    }

    @Test
    public void whenTagWithIndefiniteInputTypeIsPassed_CreateErrorDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(INDEFINITE);
        Tag tag = new Tag("key", "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(OnTagActionListener.class));

        //Then
        verify(dialogFactory).createErrorDialog(activity);
    }

    @Test
    public void whenPropertextIsSet_ValidationPassed() {
        //Given
        OnTagActionListener listener = spy(OnTagActionListener.class);

        TagType tagType = TagType.build(INDEFINITE);
        tagType = spy(tagType);
        when(tagType.getValidationRegexp()).thenReturn(".*");

        Tag tag = new Tag("key", "value", tagType);

        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.text_dialog,
                null,
                false);
        binding.setViewModel(viewModel);

        //When
        dialogFactory.validateText(
                dialog,
                binding,
                viewModel,
                tag,
                mock(OnTagActionListener.class),
                "test");

        //Then
        verify(listener).onEditEnded(tag);
    }
}
