package com.invisibleteam.goinvisible.mvvm.settings;


import android.databinding.ObservableBoolean;

public class SettingsViewModel {

    private final ObservableBoolean isClearServiceEnabled = new ObservableBoolean(false);

    public void onCheckedChanged(boolean isChecked) {
        isClearServiceEnabled.set(isChecked);
    }

    public ObservableBoolean getIsClearServiceEnabled() {
        return isClearServiceEnabled;
    }

    void setIsClearServiceEnabled(boolean isClearServiceEnabled) {
        this.isClearServiceEnabled.set(isClearServiceEnabled);
    }
}
