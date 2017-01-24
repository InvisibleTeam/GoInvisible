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

    private ImagesViewModel imagesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        Object lastNonConfigurationInstanceObject = getLastCustomNonConfigurationInstance();
        if (lastNonConfigurationInstanceObject == null) {
            imagesViewModel = new ImagesViewModel(
                    imagesCompoundRecyclerView,
                    new ImagesProvider(getContentResolver()),
                    this);
        } else {
            imagesViewModel = (ImagesViewModel) lastNonConfigurationInstanceObject;
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return imagesViewModel;
    }

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(this, imageDetails);
        startActivity(intent);
    }
}
