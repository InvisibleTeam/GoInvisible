package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditDialogInterface;

import javax.annotation.Nullable;

public class EditDialog extends DialogFragment {

    public static final String FRAGMENT_TAG = EditDialog.class.getName();
    public static final String EXTRA_TAG = "extra_tag";
    public static final String TAG = EditDialog.class.getSimpleName();

    private EditDialogInterface editDialogInterface;

    public static EditDialog newInstance(Context context, Tag tag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditDialog.EXTRA_TAG, tag);

        return (EditDialog) Fragment.instantiate(context, FRAGMENT_TAG, bundle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogFactory factory = new DialogFactory(this, extractTag(), editDialogInterface);

        Dialog dialog = factory.createDialog();
        if (dialog == null) {
            onEditError();
            return super.onCreateDialog(savedInstanceState);
        }
        return dialog;
    }

    private void onEditError() {
        setShowsDialog(false);
        dismiss();
        editDialogInterface.onEditError();
    }

    @Nullable
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Tag extractTag() {
        Bundle bundle = getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            Parcelable extra = bundle.getParcelable(EXTRA_TAG);
            if (extra instanceof Tag) {
                return (Tag) extra;
            }
        }
        Log.e(TAG, "There is some tag extraction error");
        return null;
    }

    public void setDialogInterface(EditDialogInterface editDialogInterface) {
        this.editDialogInterface = editDialogInterface;
    }
}
