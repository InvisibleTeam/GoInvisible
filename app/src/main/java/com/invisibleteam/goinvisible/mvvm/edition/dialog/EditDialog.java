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
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

public class EditDialog extends DialogFragment {

    public static final String FRAGMENT_TAG = EditDialog.class.getName();
    public static final String EXTRA_TAG = "extra_tag";
    public static final String TAG = EditDialog.class.getSimpleName();

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Tag tag;
    private OnTagActionListener listener;

    public static EditDialog newInstance(Context context, Tag tag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditDialog.EXTRA_TAG, tag);

        return (EditDialog) Fragment.instantiate(context, FRAGMENT_TAG, bundle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        extractTag();
        DialogFactory factory = new DialogFactory();
        return factory.createDialog(this, tag, listener);
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
        Log.e(TAG, "There is some tag extraction error");
        return false;
    }

    public void setActionTagListener(OnTagActionListener actionTagListener) {
        listener = actionTagListener;
    }
}
