package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.media.ExifInterface.TAG_ORIENTATION;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_TEXT_VARIATION_FILTER;
import static com.invisibleteam.goinvisible.model.InputType.DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.InputType.DATE_STRING;
import static com.invisibleteam.goinvisible.model.InputType.INDEFINITE;
import static com.invisibleteam.goinvisible.model.InputType.RANGED_INTEGER;
import static com.invisibleteam.goinvisible.model.InputType.TEXT_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TIMESTAMP_STRING;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_DOUBLE;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_INTEGER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DialogFactoryTest {

    private Activity activity;
    private DialogFragment dialog;
    private DialogFactory dialogFactory;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Activity.class).create().get();

        dialog = EditDialog.newInstance(activity, createTag(TEXT_STRING));
        dialog.show(activity.getFragmentManager(), EditDialog.FRAGMENT_TAG);
        dialog = spy(dialog);
        when(dialog.getActivity()).thenReturn(activity);

        dialogFactory = spy(new DialogFactory());
    }

    @Test
    public void whenTagWithTextStringInputTypeIsPassed_CreateTextDialogWithTextKeyboardIsCalled() {
        //Given
        Tag tag = createTag(TEXT_STRING);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createDialog(
                eq(activity),
                eq(dialog),
                eq(TYPE_TEXT_VARIATION_FILTER),
                eq(tag),
                any(OnTagActionListener.class));
    }

    @Test
    public void whenTagWithValueIntegerInputTypeIsPassed_CreateTextDialogWithNumberKeyboardIsCalled() {
        //Given
        Tag tag = createTag(VALUE_INTEGER);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createDialog(
                eq(activity),
                eq(dialog),
                eq(TYPE_CLASS_NUMBER),
                eq(tag),
                any(OnTagActionListener.class));
    }

    @Test
    public void whenTagWithValueDoubleInputTypeIsPassed_CreateTextDialogWithdecimalNumberKeyboardIsCalled() {
        //Given
        Tag tag = createTag(VALUE_DOUBLE);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createDialog(
                eq(activity),
                eq(dialog),
                eq(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL),
                eq(tag),
                any(EditViewModel.class));
    }

    @Test
    public void whenTagWithTimestampStringInputTypeIsPassed_CreateTimeDialogIsCalled() {
        //Given
        Tag tag = createTag(TIMESTAMP_STRING);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createTimeDialog();
    }

    @Test
    public void whenTagWithDateStringInputTypeIsPassed_CreateDateDialogIsCalled() {
        //Given
        Tag tag = createTag(DATE_STRING);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createDateDialog();
    }

    @Test
    public void whenTagWithDatetimeStringInputTypeIsPassed_CreateDateTimeDialogIsCalled() {
        //Given
        Tag tag = createTag(DATETIME_STRING);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createDateTimeDialog();
    }

    @Test
    public void whenTagWithRangedIntegerInputTypeIsPassed_CreateRangedDialogIsCalledWithError() {
        //Given
        Tag tag = createTag(RANGED_INTEGER);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createRangedDialog(
                eq(activity),
                eq(tag),
                any(EditViewModel.class));
        verify(dialogFactory).createErrorDialog(activity);
    }

    @Test
    public void whenTagWithRangedIntegerInputTypeIsPassed_CreateRangedDialogIsCalled() {
        //Given
        TagType tagType = TagType.build(RANGED_INTEGER);
        Tag tag = new Tag(TAG_ORIENTATION, "value", tagType);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createRangedDialog(
                eq(activity),
                eq(tag),
                any(EditViewModel.class));
        verify(dialogFactory, times(0)).createErrorDialog(activity);
    }

    @Test
    public void whenNullTagIsPassed_CreateErrorDialogIsCalled() {
        //When
        dialogFactory.createDialog(dialog, null, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createErrorDialog(activity);
    }

    @Test
    public void whenTagWithIndefiniteInputTypeIsPassed_CreateErrorDialogIsCalled() {
        //Given
        Tag tag = createTag(INDEFINITE);

        //When
        dialogFactory.createDialog(dialog, tag, mock(EditViewModel.class));

        //Then
        verify(dialogFactory).createErrorDialog(activity);
    }

    @Test
    public void whenProperTextIsSet_ValidationPassed() {
        //Given
        OnTagActionListener listener = spy(OnTagActionListener.class);

        Tag tag = createTag(".*");

        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        TextDialogBinding binding = creteBinding();
        binding.setViewModel(viewModel);

        //When
        dialogFactory.validateText(
                dialog,
                binding,
                viewModel,
                tag,
                listener,
                "test");

        //Then
        verify(listener).onEditEnded(tag);
        verify(dialog).dismiss();
    }

    @Test
    public void whenUnoroperTextIsSet_ValidationNotPassed() {
        //Given
        OnTagActionListener listener = spy(OnTagActionListener.class);

        Tag tag = createTag("0|^[1-9]([0-9]{0,5})$");

        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        TextDialogBinding binding = creteBinding();
        binding.setViewModel(viewModel);

        //When
        dialogFactory.validateText(
                dialog,
                binding,
                viewModel,
                tag,
                listener,
                "test");

        //Then
        verify(listener, times(0)).onEditEnded(tag);
        verify(dialog, times(0)).dismiss();
    }

    private Tag createTag(InputType textString) {
        TagType tagType = TagType.build(textString);
        return new Tag("key", "value", tagType);
    }

    private Tag createTag(String regexp) {
        TagType tagType = TagType.build(INDEFINITE);
        tagType = spy(tagType);
        when(tagType.getValidationRegexp()).thenReturn(regexp);

        return new Tag("key", "value", tagType);
    }

    private TextDialogBinding creteBinding() {
        return DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.text_dialog,
                null,
                false);
    }
}
