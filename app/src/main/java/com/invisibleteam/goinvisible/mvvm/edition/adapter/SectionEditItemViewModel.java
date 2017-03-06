package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import com.invisibleteam.goinvisible.util.ObservableString;

public class SectionEditItemViewModel {

    private final ObservableString tagSectionName = new ObservableString("");

    void setSectionName(String sectionName) {
        this.tagSectionName.set(sectionName);
    }

    public ObservableString getTagSectionName() {
        return tagSectionName;
    }
}
