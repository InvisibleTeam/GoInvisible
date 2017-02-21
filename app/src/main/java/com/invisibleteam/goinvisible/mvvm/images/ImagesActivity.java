package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;

public class ImagesActivity extends CommonActivity {

    public final static String TAGS_CHANGED_EXTRA = "tagsChangedExtra";
    private ImagesCallback imagesCallback;

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra(ImagesActivity.TAGS_CHANGED_EXTRA, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public void prepareView() {

        ActivityImagesBinding activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        setSupportActionBar(activityImagesBinding.mainToolbar);

        imagesCallback = buildImagesCallback(activityImagesBinding.swipeRefreshLayout);

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                imagesCallback);
        activityImagesBinding.setViewModel(imagesViewModel);
        activityImagesBinding.swipeRefreshLayout.setOnRefreshListener(imagesViewModel::updateImages);

        extractBundle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                openSettingActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettingActivity() {
        Intent intent = SettingsActivity.buildIntent(this);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.images_menu, menu);
        return true;
    }

    void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(TAGS_CHANGED_EXTRA)) {
                imagesCallback.prepareSnackBar(R.string.tags_changed_message);
                imagesCallback.showSnackBar();
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback buildImagesCallback(SwipeRefreshLayout layout) {
        return new ImagesCallback(this, layout);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ImagesCallback getImagesCallback() {
        return imagesCallback;
    }
}
