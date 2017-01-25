package com.invisibleteam.goinvisible.mvvm.images;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.invisibleteam.goinvisible.model.ImageDetails;

import java.util.ArrayList;
import java.util.List;

class ImagesProvider {
    private final ContentResolver contentResolver;

    ImagesProvider(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    List<ImageDetails> getImagesList() {
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.MIME_TYPE};
        Cursor cur = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                null);
        List<ImageDetails> imagesList = new ArrayList<>();
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    String title = cur.getString(0);
                    String data = cur.getString(1);
                    String mimeType = cur.getString(2);
                    imagesList.add(new ImageDetails(data, title));
                    if (!mimeType.endsWith("png")) {
                        imagesList.add(new ImageDetails(data, title));
                    }
                } while (cur.moveToNext());
            }
            cur.close();
        }
        return imagesList;
    }
}
