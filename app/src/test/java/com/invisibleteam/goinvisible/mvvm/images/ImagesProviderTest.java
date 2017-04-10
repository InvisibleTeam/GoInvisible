package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.invisibleteam.goinvisible.model.ImageDetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImagesProviderTest {

    private ImagesProvider imagesProvider;

    @Mock
    private ContentResolver contentResolver;

    @Before
    public void setUp() {
        imagesProvider = new ImagesProvider(contentResolver);
    }

    @Test
    public void whenSomePhotosExist_thenCursorWillReturnSomeData() {
        //given
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN};
        Cursor cursor = mock(Cursor.class);

        //when
        when(contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null)).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getString(0)).thenReturn("Title");
        when(cursor.getString(1)).thenReturn("Path");
        when(cursor.getInt(2)).thenReturn(0);
        List<ImageDetails> imagesList = imagesProvider.getImagesList();

        //then
        assertEquals(1, imagesList.size());
        assertNotNull(imagesList.get(0));

        String imageName = imagesList.get(0).getName();
        assertEquals("Title", imageName);

        String imagePath = imagesList.get(0).getPath();
        assertEquals("Path", imagePath);
    }

    @Test
    public void whenNoImagesFound_thenCursorWillBeEmpty() {
        //when
        List<ImageDetails> imagesList = imagesProvider.getImagesList();

        //then
        assertTrue(imagesList.isEmpty());
    }
}