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
import com.invisibleteam.goinvisible.util.DialogRangedValuesUtil;

import java.util.Locale;

import javax.annotation.Nullable;

class DialogFactory {

    private final DialogFragment dialog;
    private final Context context;
    private final EditViewModel viewModel;
    private DateDialog dateDialog;
    @Nullable
    private Tag tag;

    DialogFactory(DialogFragment dialog, @Nullable Tag tag, EditViewModel viewModel) {
        this.dialog = dialog;
        this.tag = tag;
        this.viewModel = viewModel;
        context = dialog.getActivity();
    }

    Dialog createDialog() {
        if (tag == null) {
            return createErrorDialog();
        }
        if (dateDialog == null) {
            dateDialog = new DateDialog(context, tag, viewModel);
        }
        InputType inputType = tag.getTagType().getInputType();

        switch (inputType) {
            case TEXT_STRING:
                return createDialog(android.text.InputType.TYPE_TEXT_VARIATION_FILTER);

            case VALUE_INTEGER:
                return createDialog(android.text.InputType.TYPE_CLASS_NUMBER);

            case VALUE_DOUBLE:
                return createDialog(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

            case RANGED_INTEGER:
                return createRangedDialog(context, tag, viewModel);

            case TIMESTAMP_STRING:
                return createTimeDialog();

            case DATETIME_STRING:
                return createDateTimeDialog();

            case DATE_STRING:
                return createDateDialog();

            case RATIONAL:
                return createDialog(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

            default:
                return createErrorDialog();
        }
    }

    Dialog createRangedDialog(Context context, Tag tag, EditViewModel viewModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(tag.getKey());

        String[] values = DialogRangedValuesUtil.getValues(tag.getKey());
        if (values == null || values.length == 0) {
            return createErrorDialog();
        }
        alertDialog.setItems(values, (dialog, index) -> {
            dialog.dismiss();
            tag.setValue(values[index]);
            viewModel.onEditEnded(tag);
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
    Dialog createDialog(int keyboardInputType) {

        //Viewmodel
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);

        //Binding configuration
        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.text_dialog,
                null,
                false);
        binding.setViewModel(viewModel);
        binding.valueText.setInputType(keyboardInputType);
        binding.okButton.setOnClickListener(v -> {
            validateText(binding, viewModel);
        });
        binding.cancelButton.setOnClickListener(v -> dialog.dismiss());

        //Dialog builder
        return new AlertDialog
                .Builder(context, R.style.AlertDialogStyle)
                .setView(binding.getRoot())
                .create();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void validateText(TextDialogBinding binding,
                      TextDialogViewModel viewModel) {
        String validationRegexp = tag.getTagType().getValidationRegexp();
        String textValue = binding.valueText.getText().toString();

        if (textValue.matches(validationRegexp)) {
            tag.setValue(textValue);
            this.viewModel.onEditEnded(tag);
            dialog.dismiss();
            return;
        }
        viewModel.setIsError(true);
        binding.valueTextLayout.setError("Error");
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Dialog createErrorDialog() {
        return new AlertDialog
                .Builder(context, R.style.AlertDialogStyle)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message)
                .setPositiveButton(context.getString(android.R.string.ok).toUpperCase(Locale.getDefault()), null)
                .setCancelable(false)
                .show();
    }
}
