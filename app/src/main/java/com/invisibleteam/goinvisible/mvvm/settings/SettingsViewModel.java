package com.invisibleteam.goinvisible.mvvm.settings;


import android.databinding.ObservableBoolean;

public class SettingsViewModel {

    private final ObservableBoolean isEnabled = new ObservableBoolean(false);

    public ObservableBoolean getIsEnabled() {
        return isEnabled;
    }

    void setIsEnabled(boolean isEnabled) {
        this.isEnabled.set(isEnabled);
    }
}
