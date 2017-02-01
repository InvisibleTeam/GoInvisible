package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;

public class EditDialog extends DialogFragment {

    public static final String FRAGMENT_TAG = EditDialog.class.getName();
    public static final String EXTRA_TAG = "extra_tag";

    public static EditDialog newInstance(Context context, Tag tag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditDialog.EXTRA_TAG, tag);

        return (EditDialog) Fragment.instantiate(context, FRAGMENT_TAG, bundle);
    }

    private Tag tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!extractTag()) {
            throw new IllegalStateException("Tag is null");
        }

        InputType inputType = tag.getTagType().getInputType();
        switch (inputType) {
            case TEXT_STRING:
                //todo change layout and view model
                break;
            case DATE_STRING:
                //todo change layout and view model
                break;
            case BOOLEAN_INTEGER:
                //todo change layout and view model
                break;
            case VALUE_INTEGER:
                //todo change layout and view model
                break;
            case VALUE_DOUBLE:
                //todo change layout and view model
                break;
            case POSITION_DOUBLE:
                //todo change layout and view model
                break;
            default:
                //todo do sth
                break;
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private boolean extractTag() {
        if (getArguments() != null && !getArguments().isEmpty()) {
            Parcelable extra = getArguments().getParcelable(EXTRA_TAG);
            if (extra instanceof Tag) {
                tag = (Tag) extra;
                return true;
            }
        }
        return false;
    }
}
