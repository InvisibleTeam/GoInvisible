package com.invisibleteam.goinvisible.util.binding;


import android.databinding.BindingAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import javax.annotation.Nullable;

public class ToolbarViewBindingAdapter {
    @BindingAdapter({"android:title"})
    public static void bindTitle(Toolbar toolbar, @Nullable ObservableString title) {
        if (title != null) {
            Log.i("ToolbarView", "bind");
            toolbar.setTitle(title.get());
        }
    }
}
