package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.support.media.ExifInterface.TAG_ORIENTATION;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
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

    private android.app.Activity activity;
    private DialogFragment dialog;
    private DialogFactory dialogFactory;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(android.app.Activity.class).create().get();
        dialog = EditDialog.newInstance(activity, createTag(TEXT_STRING));
        dialog.show(activity.getFragmentManager(), EditDialog.FRAGMENT_TAG);
        dialog = spy(dialog);
        when(dialog.getActivity()).thenReturn(activity);
    }

    @Test
    public void whenTagWithTextStringInputTypeIsPassed_CreateTextDialogWithTextKeyboardIsCalled() {
        //Given
        Tag tag = createTag(TEXT_STRING);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createDialog(eq(TYPE_TEXT_VARIATION_FILTER));
    }

    @Test
    public void whenTagWithValueIntegerInputTypeIsPassed_CreateTextDialogWithNumberKeyboardIsCalled() {
        //Given
        Tag tag = createTag(VALUE_INTEGER);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createDialog(eq(TYPE_CLASS_NUMBER));
    }

    @Test
    public void whenTagWithValueDoubleInputTypeIsPassed_CreateTextDialogWithdecimalNumberKeyboardIsCalled() {
        //Given
        Tag tag = createTag(VALUE_DOUBLE);

        //When
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createDialog(eq(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL));
    }

    @Test
    public void whenTagWithTimestampStringInputTypeIsPassed_CreateTimeDialogIsCalled() {
        //Given
        Tag tag = createTag(TIMESTAMP_STRING);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createTimeDialog();
    }

    @Test
    public void whenTagWithDateStringInputTypeIsPassed_CreateDateDialogIsCalled() {
        //Given
        Tag tag = createTag(DATE_STRING);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createDateDialog();
    }

    @Test
    public void whenTagWithDatetimeStringInputTypeIsPassed_CreateDateTimeDialogIsCalled() {
        //Given
        Tag tag = createTag(DATETIME_STRING);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createDateTimeDialog();
    }

    @Test
    public void whenTagWithRangedIntegerInputTypeWithWrongKeyIsPassed_CreateRangedDialogReturnsNull() {
        //Given
        Tag tag = createTag(RANGED_INTEGER);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        Dialog dialog = dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createRangedDialog(any(), any());
        assertNull(dialog);
    }

    @Test
    public void whenTagWithRangedIntegerInputTypeIsPassed_CreateRangedDialogReturnsDialogInstance() {
        //Given
        TagType tagType = TagType.build(RANGED_INTEGER);
        Tag tag = new Tag(TAG_ORIENTATION, "value", tagType, TagGroupType.ADVANCED);
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        Dialog dialog = dialogFactory.createDialog();

        //Then
        verify(dialogFactory).createRangedDialog(
                eq(activity),
                eq(tag));
        assertNotNull(dialog);
    }

    @Test
    public void whenTagWithIndefiniteInputTypeIsPassed_NullDialogIsReturned() {
        //Given
        Tag tag = createTag(INDEFINITE);
        when(dialog.getActivity()).thenReturn(Mockito.mock(Activity.class));
        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        //When
        Dialog dialog = dialogFactory.createDialog();

        //Then
        assertNull(dialog);
    }

    @Test
    public void whenProperTextIsSet_ValidationPassed() {
        //Given
        EditViewModel editViewModel = mock(EditViewModel.class);
        Tag tag = createTag(".*");

        dialogFactory = spy(new DialogFactory(dialog, tag, editViewModel));
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        TextDialogBinding binding = createBinding();
        binding.setViewModel(viewModel);

        //When
        dialogFactory.validateText(binding, viewModel);

        //Then
        verify(editViewModel).onEditEnded(tag);
        verify(dialog).dismiss();
    }

    @Test
    public void whenUnproperTextIsSet_ValidationNotPassed() {
        //Given
        EditViewModelCallback listener = spy(EditViewModelCallback.class);

        Tag tag = createTag("0|^[1-9]([0-9]{0,5})$");

        dialogFactory = spy(new DialogFactory(dialog, tag, mock(EditViewModel.class)));

        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        TextDialogBinding binding = createBinding();
        binding.setViewModel(viewModel);

        //When
        dialogFactory.validateText(binding, viewModel);

        //Then
        verify(listener, times(0)).onEditEnded(tag);
        verify(dialog, times(0)).dismiss();
    }

    private Tag createTag(InputType textString) {
        TagType tagType = TagType.build(textString);
        return new Tag("key", "value", tagType, TagGroupType.ADVANCED);
    }

    private Tag createTag(String regexp) {
        TagType tagType = TagType.build(INDEFINITE);
        tagType = spy(tagType);
        when(tagType.getValidationRegexp()).thenReturn(regexp);

        return new Tag("key", "value", tagType, TagGroupType.ADVANCED);
    }

    private TextDialogBinding createBinding() {
        return DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.text_dialog,
                null,
                false);
    }
}
