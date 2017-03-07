package com.invisibleteam.goinvisible.helper;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.model.ImageDetails;

import java.io.FileNotFoundException;

import static android.provider.MediaStore.Images.Media.insertImage;

public class SharingHelper {

    private static final String IMAGE_EMPTY_DESCRIPTION = "";
    private static final String SHARE_IMAGE_JPG_TYPE = "image/jpg";

    public Intent buildShareImageIntent(ImageDetails details, ContentResolver contentResolver) throws
            FileNotFoundException {
        final String imagePath = prepareImagePathToShare(details, contentResolver);
        final Uri imageUri = Uri.parse(imagePath);
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType(SHARE_IMAGE_JPG_TYPE);

        return intent;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public String prepareImagePathToShare(ImageDetails details, ContentResolver contentResolver) throws
            FileNotFoundException {
        return insertImage(
                contentResolver,
                details.getPath(),
                details.getName(),
                IMAGE_EMPTY_DESCRIPTION);
    }
}
