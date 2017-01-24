package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {
    private List<Tag> tagsList;

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
        holder.itemView.setTag(tagsList.get(position));
        holder.editItemViewModel.setModel(tagsList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    void updateImageList(List<Tag> imageList) {
        this.tagsList = imageList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private EditItemViewBinding editItemViewBinding;
        private EditItemViewModel editItemViewModel;

        ViewHolder(View editView) {
            super(editView);

            editItemViewModel = new EditItemViewModel();
            editItemViewBinding = EditItemViewBinding.bind(editView);
            editItemViewBinding.setViewModel(editItemViewModel);
        }

    }
}
