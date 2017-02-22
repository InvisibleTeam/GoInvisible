package com.invisibleteam.goinvisible.mvvm.common;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class RadioDialogBuilder {

    final private AlertDialog.Builder alertDialog;
    final private String[] tagNames;
    private int selectedValueIndex;
    private RadioDialogCallback listener;

    public RadioDialogBuilder(Context context, String[] tagNames, int index, RadioDialogCallback listener) {
        alertDialog = new AlertDialog.Builder(context);
        this.tagNames = tagNames;
        selectedValueIndex = index;
        this.listener = listener;
    }

    public RadioDialogBuilder setTitle(String title) {
        alertDialog.setTitle(title);
        return this;
    }

    public RadioDialogBuilder setPositiveButton(CharSequence text) {
        alertDialog.setPositiveButton(text, (dialog, index) -> {
            listener.onApprove(dialog, selectedValueIndex);
        });
        return this;
    }

    public RadioDialogBuilder setNegativeButton(CharSequence text) {
        alertDialog.setNegativeButton(text, (dialog, which) -> {
            listener.onCancel(dialog);
        });
        return this;
    }

    public Dialog build() {
        alertDialog.setSingleChoiceItems(tagNames, selectedValueIndex, (dialog, index) -> {
            selectedValueIndex = index;
            listener.onClick(index);
        });
        return alertDialog.create();
    }

    public interface RadioDialogCallback {
        void onClick(int index);

        void onApprove(DialogInterface dialog, int index);

        void onCancel(DialogInterface dialog);
    }
}
