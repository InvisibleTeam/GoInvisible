package com.invisibleteam.goinvisible.util.binding;


import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

public class ObservableString extends BaseObservable {
    private String value;

    public ObservableString(String value) {
        this.value = value;
    }

    public ObservableString() {
    }

    public String get() {
        return value != null ? value : "";
    }

    public void set(String value) {
        if (!equals(this.value, value)) {
            this.value = value;
            notifyChange();
        }
    }

    private static boolean equals(@Nullable Object a, @Nullable Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
