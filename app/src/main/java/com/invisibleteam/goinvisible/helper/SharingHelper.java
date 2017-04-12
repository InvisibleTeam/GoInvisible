package com.invisibleteam.goinvisible.helper;

import android.content.Intent;
import android.net.Uri;

import com.invisibleteam.goinvisible.model.ImageDetails;

import java.io.File;
import java.io.FileNotFoundException;

public class SharingHelper {

    private static final String SHARE_IMAGE_JPG_TYPE = "image/jpg";

    public Intent buildShareImageIntent(ImageDetails details) throws
            FileNotFoundException {
        Uri imageUri = Uri.fromFile(new File(details.getPath()));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType(SHARE_IMAGE_JPG_TYPE);

        return intent;
    }
}
