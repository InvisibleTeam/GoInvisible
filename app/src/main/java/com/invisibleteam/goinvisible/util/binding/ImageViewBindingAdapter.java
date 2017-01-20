package com.invisibleteam.goinvisible.util.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.invisibleteam.goinvisible.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewBindingAdapter {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, final String imageUrl) {
        Picasso.with(view.getContext())
                .load(new File(imageUrl))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(view);
    }
}
