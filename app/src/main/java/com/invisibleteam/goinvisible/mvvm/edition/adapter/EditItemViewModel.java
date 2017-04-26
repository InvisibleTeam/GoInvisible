package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.util.binding.ObservableString;

import javax.annotation.Nullable;

public class EditItemViewModel {

    private final ObservableString key = new ObservableString("");
    private final ObservableString value = new ObservableString("");
    private final ObservableBoolean isModifiable = new ObservableBoolean(true);
    @Nullable
    private EditItemGroupAdapter.ItemActionListener itemActionListener;

    private int parentPosition = 0;
    private int childPosition = 0;
    private Tag tag;

    public EditItemViewModel(@Nullable EditItemGroupAdapter.ItemActionListener itemActionListener) {
        this.itemActionListener = itemActionListener;
    }

    void setModel(int parentPosition, int childPosition, Tag tag) {
        this.parentPosition = parentPosition;
        this.childPosition = childPosition;
        this.tag = tag;

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

    public void onItemClick() {
        if (itemActionListener != null) {
            itemActionListener.onItemClick(parentPosition, childPosition, tag);
        }
    }

    public void onClearClick() {
        if (itemActionListener != null) {
            itemActionListener.onItemClearClick(parentPosition, childPosition, tag);
        }
    }
}
