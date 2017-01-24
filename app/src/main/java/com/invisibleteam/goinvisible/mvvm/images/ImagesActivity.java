package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesActivity extends AppCompatActivity implements ImagesView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);
        new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
    }

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(this, imageDetails);
        startActivity(intent);
    }
}
