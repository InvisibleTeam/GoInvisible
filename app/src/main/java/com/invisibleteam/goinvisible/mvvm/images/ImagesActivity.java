package com.invisibleteam.goinvisible.mvvm.images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditViewTabletBinding;
import com.invisibleteam.goinvisible.databinding.ImagesViewBinding;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonEditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagDiffMicroServiceFactory;
import com.invisibleteam.goinvisible.mvvm.edition.TagListDiffMicroService;
import com.invisibleteam.goinvisible.mvvm.images.phone.PhoneImagesViewCallback;
import com.invisibleteam.goinvisible.mvvm.images.phone.PhoneImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletEditViewModel;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletImagesViewCallback;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;
import com.invisibleteam.goinvisible.util.LifecycleBinder;
import com.invisibleteam.goinvisible.util.ScreenUtil;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

import rx.Observable;

public class ImagesActivity extends CommonEditActivity implements PhoneImagesViewCallback, TabletImagesViewCallback,
        EditViewModel.EditViewModelCallback,
        LifecycleBinder {

    private static final String TAG = ImagesActivity.class.getSimpleName();
    private Snackbar snackbar;
    private SwipeRefreshLayout refreshLayout;
    @Nullable
    private TabletEditViewModel editViewModel;
    private TabletImagesViewModel tabletImagesViewModel;

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
        if (ScreenUtil.isTablet(this)) {
            createTabletBinding();
        } else {
            createPhoneBinding();
        }

        prepareToolbar();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void createPhoneBinding() {
        @SuppressLint("InflateParams")
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void createTabletBinding() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_images_tablet, null);
        setContentView(viewGroup);

        ViewGroup editViewGroup = (ViewGroup) viewGroup.findViewById(R.id.edit_group);
        EditViewTabletBinding editViewTabletBinding = EditViewTabletBinding.bind(editViewGroup);
        editViewModel = new TabletEditViewModel(
                this,
                new TagDiffMicroServiceFactory(this),
                new TagListDiffMicroService(this),
                this);
        editViewTabletBinding.setViewModel(editViewModel);

        ViewGroup imagesViewGroup = (ViewGroup) viewGroup.findViewById(R.id.images_group);
        ImagesViewBinding imagesViewBinding = ImagesViewBinding.bind(imagesViewGroup);
        tabletImagesViewModel = new TabletImagesViewModel(
                imagesViewBinding.imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this,
                editViewModel);
        imagesViewBinding.setViewModel(tabletImagesViewModel);

        createRefreshLayout(imagesViewBinding, tabletImagesViewModel);

        EditActivityHelper editActivityHelper = new EditActivityHelper(this);
        setEditActivityHelper(editActivityHelper);
        setEditDialogInterface(editViewModel);
    }

    private void createRefreshLayout(ImagesViewBinding viewBinding, ImagesViewModel viewModel) {
        refreshLayout = viewBinding.swipeRefreshLayout;
        refreshLayout.setOnRefreshListener(viewModel::updateImages);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
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

    @VisibleForTesting
    void setTabletImagesViewModel(TabletImagesViewModel imagesViewModel) {
        this.tabletImagesViewModel = imagesViewModel;
    }

    @Override
    public void showEditView(ImageDetails imageDetails) {
        if (editViewModel != null) {
            try {
                editViewModel.initialize(
                        imageDetails,
                        buildTagsManager(imageDetails.getPath()));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                //TODO Log crashlitycs
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    TagsManager buildTagsManager(String path) throws IOException {
        return new TagsManager(new ExifInterface(path));
    }

    @Override
    protected void onRejectTagsChangesDialogPositive() {
        tabletImagesViewModel.onRejectTagsChangesDialogPositive();
    }

    @Override
    public void shareImage(ImageDetails imageDetails) {
        try {
            Intent intent = new SharingHelper().buildShareImageIntent(imageDetails);
            startActivity(Intent.createChooser(intent, getString(R.string.share_intent_chooser_title)));
        } catch (FileNotFoundException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            //TODO log error in crashlytics
        }
    }

    @Override
    public void showViewInEditStateInformation() {
        Snackbar.make(
                findViewById(android.R.id.content),
                R.string.save_before_share,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public <T> Observable.Transformer<T, T> bind() {
        return bindToLifecycle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (tabletImagesViewModel != null) {
            tabletImagesViewModel.onSaveInstanceState(outState);
        }
        if (editViewModel != null) {
            editViewModel.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (tabletImagesViewModel != null) {
            tabletImagesViewModel.onRestoreInstanceState(savedInstanceState);
        }
        if (editViewModel != null) {
            editViewModel.onRestoreInstanceState(savedInstanceState, ImagesActivity.this::buildTagsManager);
        }
    }
}
