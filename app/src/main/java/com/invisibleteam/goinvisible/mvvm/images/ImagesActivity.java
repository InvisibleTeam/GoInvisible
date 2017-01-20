package com.invisibleteam.goinvisible.mvvm.images;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);
        new ImagesViewModel(imagesCompoundRecyclerView, getContentResolver());
    }
}
