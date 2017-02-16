package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.util.ObservableString;

public class EditItemViewModel {

    private final ObservableString key = new ObservableString("");
    private final ObservableString value = new ObservableString("");
    private final ObservableBoolean isModifiable = new ObservableBoolean(true);

    void setModel(Tag tag) {
        this.key.set(tag.getKey());
        this.value.set(tag.getFormattedValue());
        showClearButton(tag.getTagType().getInputType());
    }

    private void showClearButton(InputType type) {
        if (InputType.UNMODIFIABLE.equals(type)) {
            this.isModifiable.set(false);
        } else {
            this.isModifiable.set(true);
        }
    }

    public ObservableString getKey() {
        return key;
    }

    public ObservableString getValue() {
        return value;
    }

    public ObservableBoolean getIsModifiable() {
        return isModifiable;
    }
}
