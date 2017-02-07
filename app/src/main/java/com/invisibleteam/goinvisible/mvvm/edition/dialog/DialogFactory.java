package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.DialogRangedValuesUtil;

import java.util.Locale;

import javax.annotation.Nullable;

class DialogFactory {

    private DateDialog dateDialog;

    Dialog createDialog(DialogFragment dialog, @Nullable Tag tag, OnTagActionListener listener) {
        Activity activity = dialog.getActivity();
        if (tag == null) {
            return createErrorDialog(activity);
        }
        dateDialog = new DateDialog(activity, tag, listener);
        InputType inputType = tag.getTagType().getInputType();

        switch (inputType) {
            case TEXT_STRING:
                return createDialog(
                        activity,
                        dialog,
                        android.text.InputType.TYPE_TEXT_VARIATION_FILTER,
                        tag,
                        listener);

            case VALUE_INTEGER:
                return createDialog(
                        activity,
                        dialog,
                        android.text.InputType.TYPE_CLASS_NUMBER,
                        tag,
                        listener);

            case VALUE_DOUBLE:
                return createDialog(
                        activity,
                        dialog,
                        android.text.InputType.TYPE_CLASS_NUMBER |  android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        tag,
                        listener);

            case RANGED_INTEGER:
                return createRangedDialog(activity, tag, listener);

            case TIMESTAMP_STRING:
                return createTimeDialog();

            case DATETIME_STRING:
                return createDateTimeDialog();

            case DATE_STRING:
                return createDateDialog();

            default:
                return createErrorDialog(activity);
        }
    }

    Dialog createRangedDialog(Activity activity, Tag tag, OnTagActionListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(tag.getKey());

        String[] values = DialogRangedValuesUtil.getValues(tag.getKey());
        if (values == null || values.length == 0) {
            return createErrorDialog(activity);
        }
        alertDialog.setItems(values, (dialog, index) -> {
            dialog.dismiss();
            tag.setValue(values[index]);
            listener.onEditEnded(tag);
        });

        return alertDialog.show();
    }

    Dialog createDateDialog() {
        return dateDialog.createDateDialog();
    }

    Dialog createTimeDialog() {
        return dateDialog.createTimeDialog();
    }

    Dialog createDateTimeDialog() {
        return dateDialog.createDateTimeDialog();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Dialog createDialog(
            Activity activity,
            DialogFragment dialog,
            int keyboardInputType,
            Tag tag,
            OnTagActionListener listener) {

        //Viewmodel
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        //Binding configuration
        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.text_dialog,
                null,
                false);
        binding.setViewModel(viewModel);
        binding.valueText.setInputType(keyboardInputType);
        binding.okButton.setOnClickListener(v -> {
            validateText(
                    dialog,
                    binding,
                    viewModel,
                    tag,
                    listener,
                    binding.valueText
                            .getText()
                            .toString());
        });
        binding.cancelButton.setOnClickListener(v -> dialog.dismiss());

        //Dialog builder
        return new AlertDialog
                .Builder(activity, R.style.AlertDialogStyle)
                .setView(binding.getRoot())
                .create();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void validateText(DialogFragment dialog,
                      TextDialogBinding binding,
                      TextDialogViewModel viewModel,
                      Tag tag,
                      OnTagActionListener listener,
                      String textValue) {
        String validationRegexp = tag.getTagType().getValidationRegexp();

        if (textValue.matches(validationRegexp)) {
            tag.setValue(textValue);
            listener.onEditEnded(tag);
            dialog.dismiss();
            return;
        }
        viewModel.setIsError(true);
        binding.valueTextLayout.setError("Error");
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Dialog createErrorDialog(Activity activity) {
        return new AlertDialog
                .Builder(activity, R.style.AlertDialogStyle)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message)
                .setPositiveButton(activity.getString(android.R.string.ok).toUpperCase(Locale.getDefault()), null)
                .setCancelable(false)
                .show();
    }
}
