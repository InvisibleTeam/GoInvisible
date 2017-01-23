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

public class ImagesItemAdapter extends RecyclerView.Adapter<ImagesItemAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<ImageDetails> imageList;

    ImagesItemAdapter() {
        imageList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, null, false);
        itemView.setOnClickListener(view1 -> {
            if (onItemClickListener != null) {
                ImageDetails imageDetails = (ImageDetails) view1.getTag();
                onItemClickListener.onItemClick(imageDetails);
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(imageList.get(position));
        holder.imageItemViewModel.setModel(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    void updateImageList(List<ImageDetails> imageList) {
        this.imageList = imageList;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    public interface OnItemClickListener {
        void onItemClick(ImageDetails imageDetails);
    }
}
