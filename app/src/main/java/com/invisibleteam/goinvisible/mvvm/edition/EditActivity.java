package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditViewBinding;
import com.invisibleteam.goinvisible.helper.EditActivityHelper;
import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CommonEditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditTagsUpdateStatusViewCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.PhoneTagEditionStartCallback;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

public class EditActivity extends CommonEditActivity implements PhoneTagEditionStartCallback, EditTagsUpdateStatusViewCallback {

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG_MODEL = "tag";
    private static final String TAG = EditActivity.class.getSimpleName();

    private EditActivityHelper editActivityHelper;
    private ImageDetails imageDetails;
    private EditMenuViewModel editMenuViewModel;
    private Tag tag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            tag = savedInstanceState.getParcelable(TAG_MODEL);
        }
        editActivityHelper = new EditActivityHelper(this);
        setEditActivityHelper(editActivityHelper);
    }

    @Override
    protected void onStop() {
        editActivityHelper.onStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(TAG_MODEL, tag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
        if (editMenuViewModel != null && editMenuViewModel.isInEditState()) {
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
                if (editMenuViewModel != null) {
                    editMenuViewModel.onApproveChangesClick();
                }
                return true;
            case R.id.menu_item_clear_all:
                if (editMenuViewModel != null) {
                    editMenuViewModel.onClearAllTagsClick();
                }
                return true;
            case R.id.menu_item_share:
                if (editMenuViewModel != null) {
                    editMenuViewModel.onShareClick();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void prepareView() {
        EditViewBinding editViewBinding = DataBindingUtil.setContentView(this, R.layout.edit_view);
        setSupportActionBar(editViewBinding.mainToolbar);

        if (extractBundle() || extractShareIntent()) {
            prepareViewModels(editViewBinding);
        } else {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.no_image_error,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
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

    private void prepareViewModels(EditViewBinding editViewBinding) {
        try {
            TagsManager tagsManager = new TagsManager(getExifInterface());

            PhoneEditViewModel editViewModel = new PhoneEditViewModel(
                    imageDetails.getName(),
                    imageDetails.getPath(),
                    editViewBinding.editCompoundRecyclerView,
                    tagsManager,
                    this);
            editViewBinding.setViewModel(editViewModel);
            setEditViewModel(editViewModel);

            editMenuViewModel = new EditMenuViewModel(
                    tagsManager,
                    editViewBinding.editCompoundRecyclerView,
                    editViewModel,
                    this);
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
            //TODO log exception to crashlitycs on else.
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ExifInterface getExifInterface() throws IOException {
        return new ExifInterface(imageDetails.getPath());
    }

    @Override
    public void onBackPressed() {
        if (editMenuViewModel != null) {
            editMenuViewModel.onBackClick();
        } else {
            finish();
        }
    }

    @Override
    protected void onRejectTagsChangesDialogPositive() {
        editMenuViewModel.onRejectTagsChangesDialogPositive();
    }

    @Override
    public void changeViewToEditMode() {
        invalidateOptionsMenu();
    }

    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void changeViewToDefaultMode() {
        invalidateOptionsMenu();
    }

    @Override
    public void showViewInEditStateInformation() {
        Snackbar.make(
                findViewById(android.R.id.content),
                R.string.save_before_share,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void shareImage() {
        try {
            Intent intent = new SharingHelper().buildShareImageIntent(imageDetails);
            startActivity(Intent.createChooser(intent, getString(R.string.share_intent_chooser_title)));
        } catch (FileNotFoundException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            //TODO log error in crashlytics
        }
    }
}
