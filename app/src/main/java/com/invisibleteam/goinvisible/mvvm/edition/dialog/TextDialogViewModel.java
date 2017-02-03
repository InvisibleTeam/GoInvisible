package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.util.ObservableString;

public class TextDialogViewModel {

    private final ObservableString key = new ObservableString("");
    private final ObservableString value = new ObservableString("");
    private ObservableBoolean isError = new ObservableBoolean(false);

    public TextDialogViewModel(Tag image) {
        this.key.set(image.getKey());
        this.value.set(image.getValue());
    }

    public ObservableString getKey() {
        return key;
    }

    public ObservableString getValue() {
        return value;
    }

    public ObservableBoolean getError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError.set(isError);
    }
}
