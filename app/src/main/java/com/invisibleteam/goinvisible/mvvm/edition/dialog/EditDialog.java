package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.TextDialogBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.Locale;

public class EditDialog extends DialogFragment {

    public static final String FRAGMENT_TAG = EditDialog.class.getName();
    public static final String EXTRA_TAG = "extra_tag";
    public static final String TAG = EditDialog.class.getSimpleName();
    private OnTagActionListener listener;

    public static EditDialog newInstance(Context context, Tag tag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditDialog.EXTRA_TAG, tag);

        return (EditDialog) Fragment.instantiate(context, FRAGMENT_TAG, bundle);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Tag tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (extractTag()) {
            return createDialog();
        }
        Log.e(TAG, "There is some tag extraction error");
        return showErrorDialog();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    boolean extractTag() {
        Bundle bundle = getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            Parcelable extra = bundle.getParcelable(EXTRA_TAG);
            if (extra instanceof Tag) {
                tag = (Tag) extra;
                return true;
            }
        }
        return false;
    }

    private Dialog createDialog() {
        InputType inputType = tag.getTagType().getInputType();
        ViewDataBinding binding;
        DateDialog dialog = new DateDialog(tag, listener, getActivity());
        switch (inputType) {
            case TEXT_STRING:
                binding = createTextDialogBinding();
                break;
            case TIMESTAMP_STRING:
                return dialog.createTimeDialog();
            case DATETIME_STRING:
                return dialog.createDateTimeDialog();
            case DATE_STRING:
                return dialog.createDateDialog();
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
            default:
                return showErrorDialog();
        }

        ((TextDialogBinding) binding).cancelButton.setOnClickListener(v -> getDialog().cancel());
        return new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogStyle)
                .setView(binding.getRoot())
                .create();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ViewDataBinding createTextDialogBinding() {
        TextDialogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()),
                R.layout.text_dialog,
                null,
                false);
        TextDialogViewModel viewModel = new TextDialogViewModel(tag);
        binding.setViewModel(viewModel);
        binding.valueText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.valueText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.valueText.getWindowToken(), 0);
            }
            return true;
        });

        binding.okButton.setOnClickListener(v -> {
            String validationRegexp = tag.getTagType().getValidationRegexp();
            String textValue = binding.valueText.getText().toString();
            if (textValue.matches(validationRegexp)) {
                tag.setValue(textValue);
                listener.onEditEnded(tag);
                dismiss();
                return;
            }
            viewModel.setIsError(true);
            binding.valueTextLayout.setError("Error");
        });

        return binding;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Dialog showErrorDialog() {
        return new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogStyle)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message)
                .setPositiveButton(getString(android.R.string.ok).toUpperCase(Locale.getDefault()), null)
                .setCancelable(false)
                .show();
    }

    public void setActionTagListener(OnTagActionListener actionTagListener) {
        listener = actionTagListener;
    }
}
