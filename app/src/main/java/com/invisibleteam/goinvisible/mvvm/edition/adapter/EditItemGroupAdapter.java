package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;

import java.util.List;

import javax.annotation.Nullable;

public class EditItemGroupAdapter extends ExpandableRecyclerAdapter<TagGroup, Tag, SectionViewHolder, TagViewHolder> {

    @Nullable
    private ItemActionListener itemActionListener = null;

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public EditItemGroupAdapter(@NonNull List<TagGroup> parentList) {
        super(parentList);
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        final View sectionItemView = LayoutInflater.from(parentViewGroup.getContext())
                .inflate(
                        R.layout.section_edit_item_view,
                        parentViewGroup,
                        false);
        return new SectionViewHolder(sectionItemView);
    }

    @NonNull
    @Override
    public TagViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        final View itemView = LayoutInflater.from(childViewGroup.getContext())
                .inflate(
                        R.layout.edit_item_view,
                        childViewGroup,
                        false);
        return new TagViewHolder(itemView, itemActionListener);
    }

    @Override
    public void onBindParentViewHolder(@NonNull SectionViewHolder parentViewHolder, int parentPosition, @NonNull TagGroup parent) {
        String groupName = parentViewHolder.itemView.getContext().getString(parent.getType().getGroupNameResId());
        parentViewHolder.setSectionName(groupName);
    }

    @Override
    public void onBindChildViewHolder(@NonNull TagViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Tag child) {
        childViewHolder.setModel(parentPosition, childPosition, child);
    }

    public void setItemActionListener(ItemActionListener itemActionListener) {
        this.itemActionListener = itemActionListener;
    }

    public interface ItemActionListener {
        void onItemClick(int parentPosition, int childPosition, Tag tag);

        void onItemClearClick(int parentPosition, int childPosition, Tag tag);
    }
}
