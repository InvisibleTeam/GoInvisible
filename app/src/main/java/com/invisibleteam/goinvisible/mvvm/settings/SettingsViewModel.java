package com.invisibleteam.goinvisible.mvvm.settings;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.util.ObservableString;

public class SettingsViewModel {

    private final ObservableBoolean isClearServiceEnabled = new ObservableBoolean(false);
    private final ObservableString intervalName = new ObservableString("");

    public void onCheckedChanged(boolean isChecked) {
        isClearServiceEnabled.set(isChecked);
    }

    public ObservableBoolean getIsClearServiceEnabled() {
        return isClearServiceEnabled;
    }

    public ObservableString getIntervalName() {
        return intervalName;
    }

    void setIntervalName(String intervalName) {
        this.intervalName.set(intervalName);
    }

    void setIsClearServiceEnabled(boolean isClearServiceEnabled) {
        this.isClearServiceEnabled.set(isClearServiceEnabled);
    }
}
