package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;

public class EditActivity extends AppCompatActivity {

    private static final String TAG_IMAGE_DETAILS = "extra_image_details";
    private ImageDetails imageDetails;

    public static Intent buildIntent(Context context, ImageDetails imageDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_IMAGE_DETAILS, imageDetails);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageDetails = extras.getParcelable(TAG_IMAGE_DETAILS);
        }
    }

    @VisibleForTesting
    ImageDetails getImageDetails() {
        return imageDetails;
    }
}
