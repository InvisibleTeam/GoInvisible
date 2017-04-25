package com.invisibleteam.goinvisible.mvvm.edition;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.NewEditViewBinding;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonEditActivity;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.util.LifecycleBinder;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

import rx.Observable;

public class EditActivity extends CommonEditActivity implements PhoneEditViewModel.PhoneViewModelCallback, LifecycleBinder {

    private static final String TAG = EditActivity.class.getSimpleName();
    private static final String TAG_IMAGE_DETAILS = "extra_image_details";

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private ImageDetails imageDetails;
    private PhoneEditViewModel editViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void prepareView() {
        NewEditViewBinding editViewBinding = DataBindingUtil.setContentView(this, R.layout.new_edit_view);
        setSupportActionBar(editViewBinding.mainToolbar);

        boolean bundleExtracted = extractBundle();
        boolean shareIntentExtracted = extractShareIntent();
        if (bundleExtracted || shareIntentExtracted) {
            prepareViewModels(editViewBinding);
            if (bundleExtracted) {
                setupHomeButton();
            }
        } else {
            showWrongImageDialog();
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

    boolean extractShareIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri == null) {
                    return false;
                }

                imageDetails = new ImagesProvider(getContentResolver()).getImage(imageUri);
                if (imageDetails != null && imageDetails.isJpeg()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void prepareViewModels(NewEditViewBinding binding) {
        try {
            TagsManager tagsManager = new TagsManager(new ExifInterface(imageDetails.getPath()));
            editViewModel = new PhoneEditViewModel(
                    imageDetails,
                    tagsManager,
                    this,
                    new TagDiffMicroServiceFactory(this),
                    new TagListDiffMicroService(this),
                    this);
            binding.setViewModel(editViewModel);
            setEditDialogInterface(editViewModel);
            setEditActivityHelper(new EditActivityHelper(this));
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
            //TODO log exception to crashlitycs on else.
        }
    }

    private void setupHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showWrongImageDialog() {
        new AlertDialog
                .Builder(EditActivity.this, R.style.AlertDialogStyle)
                .setTitle(R.string.wrong_image_error)
                .setMessage(R.string.wrong_image_error_message)
                .setPositiveButton(getText(R.string.positive_message), (dialog, which) -> finish())
                .setOnDismissListener(dialog -> finish())
                .create()
                .show();
    }

    @Override
    protected void onRejectTagsChangesDialogPositive() {
        finish();
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
            case R.id.menu_item_save_changes:
                if (editViewModel != null) {
                    editViewModel.onApproveChangesClick();
                }
                return true;
            case R.id.menu_item_clear_all:
                if (editViewModel != null) {
                    editViewModel.onClearAllClick();
                }
                return true;
            case R.id.menu_item_share:
                if (editViewModel != null) {
                    editViewModel.onShareClick();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (editViewModel != null) {
            editViewModel.onFinishScene();
        } else {
            finish();
        }
    }

    @Override
    public void updateViewState() {
        invalidateOptionsMenu();
    }

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
    public void finishScene() {
        finish();
    }

    @Override
    public <T> Observable.Transformer<T, T> bind() {
        return bindToLifecycle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        editViewModel.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editViewModel.onRestoreInstanceState(savedInstanceState);
    }
}
