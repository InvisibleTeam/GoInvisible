package com.invisibleteam.goinvisible.util.binding;


import android.databinding.BindingAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.invisibleteam.goinvisible.util.ObservableString;

public class ToolbarViewBindingAdapter {
    @BindingAdapter({"android:title"})
    public static void bindTitle(Toolbar toolbar, ObservableString title) {
        Log.i("ToolbarView", "bind");
        toolbar.setTitle(title.get());
    }
}
