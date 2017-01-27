package com.invisibleteam.goinvisible.util.binding;

import android.text.Editable;
import android.text.TextWatcher;

import com.invisibleteam.goinvisible.util.ObservableString;

class TextViewBindingTextWatcher implements TextWatcher {

    private ObservableString observableString;

    TextViewBindingTextWatcher(ObservableString observableString) {
        this.observableString = observableString;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        observableString.set(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
