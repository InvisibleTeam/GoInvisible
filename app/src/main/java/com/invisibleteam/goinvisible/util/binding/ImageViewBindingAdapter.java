package com.invisibleteam.goinvisible.util.binding;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.invisibleteam.goinvisible.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewBindingAdapter {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, ObservableString imageUrl) {
        Uri uri = Uri.fromFile(new File(imageUrl.get()));
        Log.d("ImageViewBindingAdapter", uri.toString());

        Picasso.with(view.getContext())
                .load(uri)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder_small)
                .error(R.drawable.ic_image_error_small)
                .into(view);
    }
}
