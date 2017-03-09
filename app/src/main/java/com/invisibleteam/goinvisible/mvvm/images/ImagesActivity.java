package com.invisibleteam.goinvisible.mvvm.images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
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
import com.invisibleteam.goinvisible.mvvm.edition.callback.RejectEditionChangesCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TabletEditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.mvvm.images.phone.PhoneImagesViewCallback;
import com.invisibleteam.goinvisible.mvvm.images.phone.PhoneImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletEditViewModel;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletImagesViewCallback;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;
import com.invisibleteam.goinvisible.util.ScreenUtil;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

public class ImagesActivity extends CommonEditActivity implements PhoneImagesViewCallback, TabletImagesViewCallback,
        TagEditionStartCallback, TabletEditTagCallback, RejectEditionChangesCallback {

    private static final String TAG = ImagesActivity.class.getSimpleName();
    private Snackbar snackbar;
    private SwipeRefreshLayout refreshLayout;
    private
    @Nullable
    TabletEditViewModel editViewModel;
    private TabletImagesViewModel tabletImagesViewModel;
    private SharingHelper sharingHelper;

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
        sharingHelper = new SharingHelper(getContentResolver());
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
        editViewModel = new TabletEditViewModel(editViewTabletBinding.editCompoundRecyclerView, this, sharingHelper);
        editViewTabletBinding.setViewModel(editViewModel);

        ViewGroup imagesViewGroup = (ViewGroup) viewGroup.findViewById(R.id.images_group);
        ImagesViewBinding imagesViewBinding = ImagesViewBinding.bind(imagesViewGroup);
        tabletImagesViewModel = new TabletImagesViewModel(
                imagesViewBinding.imagesCompoundRecyclerView,
                new ImagesProvider(getContentResolver()),
                this,
                editViewTabletBinding.editCompoundRecyclerView);
        imagesViewBinding.setViewModel(tabletImagesViewModel);

        createRefreshLayout(imagesViewBinding, tabletImagesViewModel);

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

    @VisibleForTesting
    void setTabletImagesViewModel(TabletImagesViewModel imagesViewModel) {
        this.tabletImagesViewModel = imagesViewModel;
    }

    @VisibleForTesting
    void setTabletEditViewModel(TabletEditViewModel editViewModel) {
        this.editViewModel = editViewModel;
    }

    @Override
    public void showEditView(ImageDetails imageDetails) {
        if (editViewModel != null) {
            try {
                editViewModel.initialize(
                        imageDetails,
                        buildTagsManager(imageDetails.getPath()),
                        this);
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
    public void changeViewToDefaultMode() {
        if (editViewModel != null) {
            editViewModel.changeViewToDefaultMode();
        }
        //onElse TODO Log crashlitycs
    }

    @Override
    protected void onRejectTagsChangesDialogPositive() {
        tabletImagesViewModel.onRejectTagsChangesDialogPositive();
    }

    @Override
    public void onShare(ImageDetails details, SharingHelper sharingHelper) {
        try {
            Intent intent = sharingHelper.buildShareImageIntent(details);
            startActivity(Intent.createChooser(intent, getString(R.string.share_intent_chooser_title)));
        } catch (FileNotFoundException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            //TODO log error in crashlytics
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharingHelper.onStop();
    }
}
