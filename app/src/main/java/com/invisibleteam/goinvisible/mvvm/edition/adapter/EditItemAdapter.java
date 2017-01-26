package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.ArrayList;
import java.util.List;

class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {
    private List<Tag> tagsList;
    private OnTagActionListener onTagActionListener;

    EditItemAdapter() {
        tagsList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, null, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tag tag = tagsList.get(position);

        holder.itemView.setTag(tag);
        holder.editItemViewModel.setModel(tag);
        holder.editItemViewBinding.clearButton.setOnClickListener(v -> onTagActionListener.onClear(tag));
        // TODO "think about moving it to onCreateViewHolder in the future"
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    void updateImageList(List<Tag> imageList) {
        this.tagsList = imageList;
    }

    void setOnTagActionListener(OnTagActionListener listener) {
        this.onTagActionListener = listener;
    }

    void updateTag(Tag tag) {
        if (!tagsList.isEmpty()) {
            for (int index = 0; index < tagsList.size(); index++) {
                if (tagsList.get(index).getKey().equals(tag.getKey())) {
                    tagsList.set(index, tag);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        EditItemViewModel editItemViewModel;
        private EditItemViewBinding editItemViewBinding;

        ViewHolder(View editView) {
            super(editView);

            editItemViewModel = new EditItemViewModel();
            editItemViewBinding = EditItemViewBinding.bind(editView);
            editItemViewBinding.setViewModel(editItemViewModel);
        }
    }

    @VisibleForTesting
    List<Tag> getTagsList() {
        return tagsList;
    }
}
