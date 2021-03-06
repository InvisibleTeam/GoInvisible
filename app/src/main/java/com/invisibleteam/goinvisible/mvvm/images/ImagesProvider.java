package com.invisibleteam.goinvisible.mvvm.images;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.invisibleteam.goinvisible.model.ImageDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class ImagesProvider {
    private final ContentResolver contentResolver;

    public ImagesProvider(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Nullable
    public ImageDetails getImage(Uri imageUri) {
        ImageDetails imageDetails = null;

        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN};
        Cursor cur = contentResolver.query(
                imageUri,
                columns,
                null,
                null,
                null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String title = cur.getString(0);
                String data = cur.getString(1);
                int timestamp = cur.getInt(2);
                if (data != null) {
                    if (title == null) {
                        title = Uri.parse(data).getLastPathSegment();
                    }
                    imageDetails = new ImageDetails(data, title, timestamp);
                }
            }
            cur.close();
        }
        return imageDetails;
    }

    public List<ImageDetails> getImagesList() {
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN};
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
                    int timestamp = cur.getInt(2);
                    imagesList.add(new ImageDetails(data, title, timestamp));
                } while (cur.moveToNext());
            }
            cur.close();
        }
        return sort(imagesList);
    }

    private List<ImageDetails> sort(List<ImageDetails> imagesList) {
        ImageDetails[] array = new ImageDetails[imagesList.size()];
        imagesList.toArray(array);
        Arrays.sort(array, (firstItem, secondItem) -> {
                    if (firstItem.getTimeStamp() > secondItem.getTimeStamp()) {
                        return -1;
                    } else if (firstItem.getTimeStamp() == secondItem.getTimeStamp()) {
                        return 0;
                    }
                    return 1;
                }
        );
        return Arrays.asList(array);
    }
}
