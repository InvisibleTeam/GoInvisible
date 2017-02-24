package com.invisibleteam.goinvisible.mvvm.images;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditViewTabletBinding;
import com.invisibleteam.goinvisible.databinding.ImagesViewBinding;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonEditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;

import java.io.IOException;

import javax.annotation.Nullable;

public class ImagesActivity extends CommonEditActivity implements PhoneImagesViewCallback, TabletImagesViewCallback,
        EditTagCallback {

    private Snackbar snackbar;
    private SwipeRefreshLayout refreshLayout;
    private
    @Nullable
    EditViewModel editViewModel;

    public static Intent buildIntent(Context context) {
        return new Intent(context, ImagesActivity.class);
    }

    @Override
    public void openEditScreen(ImageDetails imageDetails) {
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

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            createTabletBinding();
        } else {
            createPhoneBinding();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void createPhoneBinding() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_images_phone, null);
        setContentView(viewGroup);
        ViewGroup imagesViewGroup = (ViewGroup) viewGroup.findViewById(R.id.images_group);
        ImagesViewBinding imagesViewBinding = ImagesViewBinding.bind(imagesViewGroup);

        ImagesViewModel imagesViewModel = new PhoneImagesViewModel(
                imagesViewBinding.imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
        imagesViewBinding.setViewModel(imagesViewModel);
        createRefreshLayout(imagesViewBinding, imagesViewModel);
    }

    private void createTabletBinding() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_images_tablet, null);
        setContentView(viewGroup);
        ViewGroup imagesViewGroup = (ViewGroup) viewGroup.findViewById(R.id.images_group);
        ViewGroup editViewGroup = (ViewGroup) viewGroup.findViewById(R.id.edit_group);
        ImagesViewBinding imagesViewBinding = ImagesViewBinding.bind(imagesViewGroup);

        EditViewTabletBinding editViewTabletBinding = EditViewTabletBinding.bind(editViewGroup);
        editViewModel = new EditViewModel(editViewTabletBinding.editCompoundRecyclerView);
        editViewTabletBinding.setViewModel(editViewModel);

        ImagesViewModel imagesViewModel = new TabletImagesViewModel(
                imagesViewBinding.imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this);
        imagesViewBinding.setViewModel(imagesViewModel);
        createRefreshLayout(imagesViewBinding, imagesViewModel);

        EditActivityHelper editActivityHelper = new EditActivityHelper(this);
        setEditActivityHelper(editActivityHelper);
        setEditViewModel(editViewModel);
    }

    private void createRefreshLayout(ImagesViewBinding viewBinding, ImagesViewModel viewModel) {
        refreshLayout = viewBinding.swipeRefreshLayout;
        viewBinding.swipeRefreshLayout.setOnRefreshListener(viewModel::updateImages);
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

    @Override
    public void showEditView(ImageDetails imageDetails) {
        if (editViewModel != null) {
            try {
                editViewModel.setTagsManager(new TagsManager(new ExifInterface(imageDetails.getPath())));
                editViewModel.updateRecyclerView(
                        imageDetails,
                        new TagsManager(new ExifInterface(imageDetails.getPath())),
                        this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
