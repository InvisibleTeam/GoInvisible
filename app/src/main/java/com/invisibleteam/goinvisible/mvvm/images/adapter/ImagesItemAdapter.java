package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ImageItemViewBinding;

import java.util.ArrayList;
import java.util.List;

public class ImagesItemAdapter extends RecyclerView.Adapter<ImagesItemAdapter.ViewHolder> {
    private List<String> imageList;

    public ImagesItemAdapter() {
        imageList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageItemViewModel.setName(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void updateImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageItemViewBinding imageItemViewBinding;
        private ImageItemViewModel imageItemViewModel;

        public ViewHolder(View itemView) {
            super(itemView);

            imageItemViewModel = new ImageItemViewModel();
            imageItemViewBinding = ImageItemViewBinding.bind(itemView);
            imageItemViewBinding.setViewModel(imageItemViewModel);
        }

    }
}
