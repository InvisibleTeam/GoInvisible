package com.invisibleteam.goinvisible.mvvm.common;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class RadioDialog {

    final private AlertDialog.Builder alertDialog;
    final private String[] tagNames;
    private int selectedValueIndex;
    private RadioDialogCallback listener;

    public RadioDialog(Context context, String[] tagNames, int index, RadioDialogCallback listener) {
        alertDialog = new AlertDialog.Builder(context);
        this.tagNames = tagNames;
        selectedValueIndex = index;
        this.listener = listener;
    }

    public RadioDialog setTitle(String title) {
        alertDialog.setTitle(title);
        return this;
    }

    public RadioDialog setPositiveButton(CharSequence text) {
        alertDialog.setPositiveButton(text, (dialog, index) -> {
            listener.onApprove(dialog, selectedValueIndex);
        });
        return this;
    }

    public RadioDialog setNegativeButton(CharSequence text) {
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
