package com.invisibleteam.goinvisible.tests;

import android.support.media.ExifInterface;
import android.support.test.runner.AndroidJUnit4;

import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.pages.FileView;
import com.invisibleteam.goinvisible.pages.GoInvisibleApplication;
import com.invisibleteam.goinvisible.pages.ImagesView;
import com.invisibleteam.goinvisible.utilities.StorageManagementHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;
import static com.invisibleteam.goinvisible.utilities.StorageManagementHelper.PICTURES_OUTPUT_PATH;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class FileViewTests {
    @Before
    public void setUp() throws Exception {
        GoInvisibleApplication.grantStoragePermissions();

        StorageManagementHelper.copyAssetsToLocalStorage();

        GoInvisibleApplication.launchFromHomeScreen();
    }

    @After
    public void tearDown() throws Exception {
        StorageManagementHelper.deleteAssetsFromLocalStorage();

        StorageManagementHelper.deleteImagesFromPhotoDirectory();

        UI_DEVICE.pressHome();
    }

    /**
     * Test case:
     * - Show on FileView EXIF data from file
     * <p>
     * Preconditions:
     * - GoInvisible app is opened
     * - permissions are granted
     * - there are files in device storage
     * <p>
     * Steps to execute:
     * - ensure GoInvisible ListView is opened
     * - ensure that on ListView images are displayed
     * -
     * - ensure that GoInvisible is displaying that image on ListView
     *
     * @throws Exception
     */
    @Test
    public void checkImageIsVisibleInFileView() throws Exception {
        String photoName = "NoExif";
        String photoPath = String.format("%s/%s.jpg", PICTURES_OUTPUT_PATH, photoName);

        int expectedTagCount = new TagsManager(new ExifInterface(photoPath)).getAllTags().size();

        ImagesView.isOpened();
        ImagesView.isAnyImageLoaded();

        ImagesView.openPhoto(photoName);

        assertTrue(FileView.isOpened());

        int displayedTagCount = FileView.getExifData().size();

        assertEquals(expectedTagCount, displayedTagCount);
    }

    /**
     * Test case:
     * - Check that all tags can be cleaned at once
     * <p>
     * Preconditions:
     * - GoInvisible app is opened
     * - permissions are granted
     * - there is files with EXIF data in device storage
     * <p>
     * Steps to execute:
     * - ensure GoInvisible ListView is opened
     * - ensure that images are displayed on ListView
     * - open photo with EXIF data
     * - clear all data at once
     * - ensure that EXIF data was cleared
     *
     * @throws Exception
     */
    @Test
    public void checkImageTagsCanBeAllCleared() throws Exception {
        //noinspection SpellCheckingInspection
        String photoName = "FullOfExifs";

        ImagesView.isOpened();
        ImagesView.isAnyImageLoaded();

        ImagesView.openPhoto(photoName);

        assertTrue(FileView.isOpened());

        LinkedHashMap<String, String> tagsBeforeCleaning = FileView.getExifData();

        FileView.clearAllData();

        LinkedHashMap<String, String> tagsAfterCleaning = FileView.getExifData();

        assertNotEquals(tagsAfterCleaning, tagsBeforeCleaning);
    }
}
