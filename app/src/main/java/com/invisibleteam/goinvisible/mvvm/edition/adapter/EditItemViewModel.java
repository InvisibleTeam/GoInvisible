package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.util.ObservableString;

public class EditItemViewModel {

    private final ObservableString key = new ObservableString("");
    private final ObservableString value = new ObservableString("");

    void setModel(Tag image) {
        this.key.set(image.getFormattedKey());
        this.value.set(image.getFormattedValue());
    }

    public ObservableString getKey() {
        return key;
    }

    public ObservableString getValue() {
        return value;
    }
}
