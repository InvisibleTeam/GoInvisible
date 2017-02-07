package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.Locale;

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
                return createTextDialog(activity, dialog, tag, listener);
//            case RANGED_STRING://TODO other dialog types
//                break;
//            case RANGED_INTEGER:
//                break;
//            case VALUE_INTEGER:
//                break;
//            case VALUE_DOUBLE:
//                break;
//            case POSITION_DOUBLE:
//                break;
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
    Dialog createTextDialog(Activity activity, DialogFragment dialog, Tag tag, OnTagActionListener listener) {
        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.text_dialog,
                null,
                false);
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);
        binding.setViewModel(viewModel);

        binding.okButton.setOnClickListener(v -> {
            validateText(dialog, binding, viewModel, tag, listener, binding.valueText.getText().toString());
        });

        binding.cancelButton.setOnClickListener(v -> dialog.dismiss());
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
