package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
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

    private Snackbar snackbar;
    private SwipeRefreshLayout refreshLayout;

    public static Intent buildIntent(Context context) {
        return new Intent(context, ImagesActivity.class);
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
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        ActivityImagesBinding activityImagesBinding;
        if (diagonalInches>=6.5){
            activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.images_tablet_activity);
        }else{
            activityImagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        }


//        setSupportActionBar(activityImagesBinding.mainToolbar);

        refreshLayout = activityImagesBinding.swipeRefreshLayout;

        ImagesCompoundRecyclerView imagesCompoundRecyclerView =
                (ImagesCompoundRecyclerView) findViewById(R.id.images_compound_recycler_view);

        ImagesViewModel imagesViewModel = new ImagesViewModel(
                imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
        activityImagesBinding.setViewModel(imagesViewModel);
        activityImagesBinding.swipeRefreshLayout.setOnRefreshListener(imagesViewModel::updateImages);
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
