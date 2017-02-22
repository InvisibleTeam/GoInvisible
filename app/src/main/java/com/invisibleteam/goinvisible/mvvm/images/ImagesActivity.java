package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityImagesBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;

public class ImagesActivity extends CommonActivity implements ImagesViewCallback {

    public final static String TAGS_CHANGED_EXTRA = "tagsChangedExtra";
    private Snackbar snackbar;
    private SwipeRefreshLayout refreshLayout;

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra(ImagesActivity.TAGS_CHANGED_EXTRA, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public void navigateToEdit(ImageDetails imageDetails) {
        Intent intent = EditActivity.buildIntent(this, imageDetails);
        startActivity(intent);
    }

    @Override
    public void showSnackBar() {
        snackbar.show();
    }

    @Override
    public void onStopRefreshingImages() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void prepareSnackBar(int messageResId) {
        if (snackbar == null) {
            snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    messageResId,
                    Snackbar.LENGTH_LONG);
        }
        snackbar.setText(messageResId);
    }

    @Override
    public void prepareView() {
        ActivityImagesBinding activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        setSupportActionBar(activityImagesBinding.mainToolbar);

        refreshLayout = activityImagesBinding.swipeRefreshLayout;

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.images_menu, menu);
        return true;
    }

    private void openSettingActivity() {
        Intent intent = SettingsActivity.buildIntent(this);
        startActivity(intent);
    }

    void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(TAGS_CHANGED_EXTRA)) {
                prepareSnackBar(R.string.tags_changed_message);
                showSnackBar();
            }
        }
    }

    @VisibleForTesting
    public void setRefreshLayout(SwipeRefreshLayout layout) {
        this.refreshLayout = layout;
    }

    @VisibleForTesting
    void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    @VisibleForTesting
    public Snackbar getSnackbar() {
        return snackbar;
    }
}
