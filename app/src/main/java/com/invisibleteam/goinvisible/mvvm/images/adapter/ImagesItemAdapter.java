package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ImageItemViewBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;

import java.util.ArrayList;
import java.util.List;

class ImagesItemAdapter extends RecyclerView.Adapter<ImagesItemAdapter.ViewHolder> {
    private List<ImageDetails> imageList;

    ImagesItemAdapter() {
        imageList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageItemViewModel.setModel(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    void updateImageList(List<ImageDetails> imageList) {
        this.imageList = imageList;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageItemViewBinding imageItemViewBinding;
        private ImageItemViewModel imageItemViewModel;

        protected ViewHolder(View itemView) {
            super(itemView);

            imageItemViewModel = new ImageItemViewModel();
            imageItemViewBinding = ImageItemViewBinding.bind(itemView);
            imageItemViewBinding.setViewModel(imageItemViewModel);
        }

    }
}
