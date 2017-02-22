package com.invisibleteam.goinvisible.tests;

import android.support.test.runner.AndroidJUnit4;

import com.invisibleteam.goinvisible.pages.GoInvisibleApplication;
import com.invisibleteam.goinvisible.pages.ImagesView;
import com.invisibleteam.goinvisible.pages.system.CameraView;
import com.invisibleteam.goinvisible.utilities.StorageManagementHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ListViewTests {
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
     * - Show on ListView files that are in device storage
     * <p>
     * Preconditions:
     * - GoInvisible app is opened
     * - permissions are granted
     * - there are files in device storage
     * <p>
     * Steps to execute:
     * - ensure GoInvisible ListView is opened
     * - ensure that on ListView images are displayed
     *
     * @throws Exception
     */
    @Test
    public void checkImagesAreListed() throws Exception {
        assertTrue(ImagesView.isOpened());
        assertTrue(ImagesView.isAnyImageLoaded());
    }

    /**
     * Test case:
     * - Show on ListView files that are in device storage
     * <p>
     * Preconditions:
     * - GoInvisible app is opened
     * - permissions are granted
     * - there are files in device storage
     * <p>
     * Steps to execute:
     * - ensure GoInvisible ListView is opened
     * - ensure that on ListView images are displayed
     * - make photo with built-in camera
     * - ensure that GoInvisible is displaying that image on ListView
     *
     * @throws Exception
     */
    @Test
    public void checkCameraImagesAreListed() throws Exception {
        ImagesView.isOpened();

        int numberOfImagesBeforeTakingPhoto = ImagesView.getVisibleImages().size();

        CameraView.takePhotoAndGoBack();

        ImagesView.isOpened();

        ImagesView.pullToRefresh();

        int numberOfImagesAfterTakingPhoto = ImagesView.getVisibleImages().size();

        assertTrue(numberOfImagesBeforeTakingPhoto < numberOfImagesAfterTakingPhoto);
    }
}
