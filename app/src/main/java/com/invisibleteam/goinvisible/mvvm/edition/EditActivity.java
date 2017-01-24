package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.ActivityEditBinding;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

public class EditActivity extends AppCompatActivity {

    private static final String TAG_IMAGE_DETAILS = "filePath";

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private ImageDetails imageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditBinding activityEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        setSupportActionBar(activityEditBinding.mainToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageDetails = extras.getParcelable(TAG_IMAGE_DETAILS);
        }

        EditCompoundRecyclerView editCompoundRecyclerView =
                (EditCompoundRecyclerView) findViewById(R.id.edit_compound_recycler_view);
        EditViewModel editViewModel = new EditViewModel(
                imageDetails.getName(),
                imageDetails.getPath(),
                editCompoundRecyclerView,
                new TagsProvider(imageDetails));
        activityEditBinding.setViewModel(editViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
