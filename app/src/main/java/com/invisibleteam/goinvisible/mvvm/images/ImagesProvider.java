package com.invisibleteam.goinvisible.mvvm.images;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.invisibleteam.goinvisible.model.ImageDetails;

import java.util.ArrayList;
import java.util.List;

public class ImagesProvider {
    private final ContentResolver contentResolver;

    public ImagesProvider(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public List<ImageDetails> getImagesList() {
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA};
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
                    imagesList.add(new ImageDetails(data, title));
                } while (cur.moveToNext());
            }
            cur.close();
        }
        return imagesList;
    }
}