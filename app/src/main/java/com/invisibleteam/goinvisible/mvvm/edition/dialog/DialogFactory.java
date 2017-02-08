package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.DialogRangedValuesUtil;

import java.util.Locale;

import javax.annotation.Nullable;

class DialogFactory {

    private DateDialog dateDialog;

    Dialog createDialog(DialogFragment dialog, @Nullable Tag tag, EditViewModel viewModel) {
        Context context = dialog.getActivity();
        if (tag == null) {
            return createErrorDialog(context);
        }
        dateDialog = new DateDialog(context, tag, viewModel);
        InputType inputType = tag.getTagType().getInputType();

        switch (inputType) {
            case TEXT_STRING:
                return createTextDialog(context, dialog, tag, viewModel);
            case RANGED_INTEGER:
                return createRangedDialog(context, tag, viewModel);
//            case VALUE_INTEGER://TODO other dialog types
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
                return createErrorDialog(context);
        }
    }

    Dialog createRangedDialog(Context context, Tag tag, OnTagActionListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(tag.getKey());

        String[] values = DialogRangedValuesUtil.getValues(tag.getKey());
        if (values == null || values.length == 0) {
            return createErrorDialog(context);
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
    Dialog createTextDialog(Context context, DialogFragment dialog, Tag tag, OnTagActionListener listener) {
        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.text_dialog,
                null,
                false);
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);
        binding.setViewModel(viewModel);

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
        return new AlertDialog
                .Builder(context, R.style.AlertDialogStyle)
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
    Dialog createErrorDialog(Context context) {
        return new AlertDialog
                .Builder(context, R.style.AlertDialogStyle)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message)
                .setPositiveButton(context.getString(android.R.string.ok).toUpperCase(Locale.getDefault()), null)
                .setCancelable(false)
                .show();
    }
}
