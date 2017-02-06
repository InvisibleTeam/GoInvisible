package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityEditBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CommonActivity;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import java.io.IOException;

import javax.annotation.Nullable;

public class EditActivity extends CommonActivity {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private static final String TAG = EditActivity.class.getSimpleName();

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private final EditTagListener editTagListener = tag -> {
        EditDialog dialog = EditDialog.newInstance(this, tag);
        dialog.show(getFragmentManager(), EditDialog.FRAGMENT_TAG);
    };

    private ImageDetails imageDetails;
    private EditViewModel editViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    boolean extractBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Parcelable extra = extras.getParcelable(TAG_IMAGE_DETAILS);
            if (extra instanceof ImageDetails) {
                imageDetails = extras.getParcelable(TAG_IMAGE_DETAILS);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @VisibleForTesting
    @Nullable
    EditViewModel getEditViewModel() {
        return editViewModel;
    }

    @Override
    public void prepareView() {
        ActivityEditBinding activityEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        setSupportActionBar(activityEditBinding.mainToolbar);
        if (extractBundle()) {
            EditCompoundRecyclerView editCompoundRecyclerView =
                    (EditCompoundRecyclerView) findViewById(R.id.edit_compound_recycler_view);

            try {
                ExifInterface exifInterface = new ExifInterface(imageDetails.getPath());
                editViewModel = new EditViewModel(
                        imageDetails.getName(),
                        imageDetails.getPath(),
                        editCompoundRecyclerView,
                        new TagsManager(exifInterface),
                        editTagListener);
                activityEditBinding.setViewModel(editViewModel);
            } catch (IOException e) {
                Log.d(TAG, String.valueOf(e.getMessage()));
                //TODO log exception to crashlitycs on else.
            }
        } //TODO log exception to crashlitycs on else.
        getWindow().getSharedElementEnterTransition();
    }
}
