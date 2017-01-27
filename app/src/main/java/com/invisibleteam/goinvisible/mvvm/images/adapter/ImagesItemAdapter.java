package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ImageItemViewBinding;
import com.invisibleteam.goinvisible.databinding.UnsupportedImageItemViewBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ImagesItemAdapter extends RecyclerView.Adapter<ImagesItemAdapter.ViewHolder> {

    static final int JPEG_IMAGE = 0;
    static final int UNSUPPORTED_EXTENSION_IMAGE = 1;

    private OnItemClickListener onItemClickListener;
    private List<ImageDetails> imageList;

    ImagesItemAdapter() {
        imageList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == JPEG_IMAGE) {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.image_item_view, null, false);
            itemView.setOnClickListener(view1 -> {
                if (onItemClickListener != null) {
                    ImageDetails imageDetails = (ImageDetails) view1.getTag();
                    onItemClickListener.onItemClick(imageDetails);
                }
            });
            return new JpegImageViewHolder(itemView);
        }
        itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.unsupported_image_item_view, null, false);
        itemView.setOnClickListener(view1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onUnsupportedItemClick();
            }
        });
        return new UnsupprotedImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(imageList.get(position));
        if (holder instanceof JpegImageViewHolder) {
            ((JpegImageViewHolder) holder).viewModel.setModel(imageList.get(position));
        } else if (holder instanceof UnsupprotedImageViewHolder) {
            ((UnsupprotedImageViewHolder) holder).viewModel.setModel(imageList.get(position));
            ImageUtil.grayedOut(((UnsupprotedImageViewHolder) holder).binding.image);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ImageDetails image = imageList.get(position);
        if (image.getPath().matches(".*jpg$")) {
            return JPEG_IMAGE;
        }
        return UNSUPPORTED_EXTENSION_IMAGE;
    }

    void updateImageList(List<ImageDetails> imageList) {
        this.imageList = imageList;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }

        void setupTextView(TextView textView) {
            textView.setLines(1);
            textView.setHorizontallyScrolling(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    private class JpegImageViewHolder extends ViewHolder {

        private ImageItemViewBinding binding;
        private ImageItemViewModel viewModel;

        JpegImageViewHolder(View itemView) {
            super(itemView);

            viewModel = new ImageItemViewModel();
            binding = ImageItemViewBinding.bind(itemView);

            setupTextView(binding.text);
            binding.setViewModel(viewModel);
        }
    }

    private class UnsupprotedImageViewHolder extends ViewHolder {

        private UnsupportedImageItemViewBinding binding;
        private ImageItemViewModel viewModel;

        UnsupprotedImageViewHolder(View itemView) {
            super(itemView);

            viewModel = new ImageItemViewModel();
            binding = UnsupportedImageItemViewBinding.bind(itemView);

            setupTextView(binding.text);
            binding.setViewModel(viewModel);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ImageDetails imageDetails);

        void onUnsupportedItemClick();
    }
}
