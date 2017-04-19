package com.invisibleteam.goinvisible.mvvm.edition;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.media.ExifInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.NewEditViewBinding;
import com.invisibleteam.goinvisible.helper.NewEditActivityHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.NewCommonEditActivity;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.IOException;

import javax.annotation.Nullable;

public class NewActivity extends NewCommonEditActivity implements NewEditViewModel.EditViewModelCallback {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, NewActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private ImageDetails imageDetails;
    private NewEditViewModel editViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void prepareView() {
        NewEditViewBinding editViewBinding = DataBindingUtil.setContentView(this, R.layout.new_edit_view);
        setSupportActionBar(editViewBinding.mainToolbar);
        extractBundle();
        try {
            TagsManager tagsManager = new TagsManager(new ExifInterface(imageDetails.getPath()));
            editViewModel = new NewEditViewModel(imageDetails, tagsManager, this);
            editViewBinding.setViewModel(editViewModel);
            setEditDialogInterface(editViewModel);
            setEditActivityHelper(new NewEditActivityHelper(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean extractBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Parcelable extra = extras.getParcelable(TAG_IMAGE_DETAILS);
            if (extra != null && extra instanceof ImageDetails) {
                imageDetails = extras.getParcelable(TAG_IMAGE_DETAILS);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onRejectTagsChangesDialogPositive() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
        if (editViewModel != null && editViewModel.isInEditState()) {
            showSaveChangesMenuItem(menu);
        }

        return true;
    }

    private void showSaveChangesMenuItem(Menu menu) {
        menu.findItem(R.id.menu_item_save_changes).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_item_clear_all:
                editViewModel.onClearAllClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateViewState() {
        invalidateOptionsMenu();
    }
}
