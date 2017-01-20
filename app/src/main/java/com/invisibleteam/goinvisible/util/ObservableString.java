package com.invisibleteam.goinvisible.util;


import android.databinding.BaseObservable;

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

    private static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
