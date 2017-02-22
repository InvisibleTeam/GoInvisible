package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.RadioDialog;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.util.DialogRangedValuesUtil;

import java.util.Map;

import javax.annotation.Nullable;

class DialogFactory {

    private final DialogFragment dialog;
    private final Context context;
    private final EditViewModel viewModel;
    private DateDialog dateDialog;
    private Tag tag;

    DialogFactory(DialogFragment dialog, Tag tag, EditViewModel viewModel) {
        this.dialog = dialog;
        this.tag = tag;
        this.viewModel = viewModel;
        context = dialog.getActivity();
    }

    @Nullable
    Dialog createDialog() {
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
            case RANGED_STRING:
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
                return null;
        }
    }

    @Nullable
    Dialog createRangedDialog(Context context, Tag baseTag, EditViewModel viewModel) {
        Tag tag = new Tag(baseTag);

        final Map<Integer, Integer> tagsMap = DialogRangedValuesUtil.getTagsMapValues(tag.getKey());

        if (tagsMap == null || tagsMap.isEmpty()) {
            return null;
        }

        final String[] tagNames = new String[tagsMap.size()];
        final SparseArray<String> tagValues = new SparseArray<>();
        int valuesIndex = 0;
        int selectedValueIndex = 0;

        for (Map.Entry<Integer, Integer> entry : tagsMap.entrySet()) {

            tagNames[valuesIndex] = context.getString(entry.getKey());

            String tagValue = context.getString(entry.getValue());
            tagValues.append(valuesIndex, tagValue);

            if (tagValue.equals(tag.getValue())) {
                selectedValueIndex = valuesIndex;
            }

            valuesIndex++;
        }

        RadioDialog.RadioDialogCallback callback = new RadioDialog.RadioDialogCallback() {
            @Override
            public void onClick(int index) {
                tag.setValue(tagValues.get(index));
            }

            @Override
            public void onApprove(DialogInterface dialog, int index) {
                viewModel.onEditEnded(tag);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        };

        return new RadioDialog(context, tagNames, selectedValueIndex, callback)
                .setTitle(tag.getKey())
                .setPositiveButton(context.getText(android.R.string.ok))
                .setNegativeButton(context.getText(android.R.string.cancel))
                .build();
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
        binding.valueTextLayout.setError(context.getString(R.string.error_text));
    }
}
