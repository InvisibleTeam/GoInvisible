package com.invisibleteam.goinvisible.tests;

import android.support.test.runner.AndroidJUnit4;

import com.invisibleteam.goinvisible.pages.GoInvisibleApplication;
import com.invisibleteam.goinvisible.pages.ImagesView;
import com.invisibleteam.goinvisible.utilities.UiDeviceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ListViewTests {
    @Before
    public void setUp() throws Exception {
        GoInvisibleApplication.grantStoragePermissions();

        GoInvisibleApplication.copyAssetsToLocalStorage();

        GoInvisibleApplication.launchFromHomeScreen();
    }

    @After
    public void tearDown() throws Exception {
        GoInvisibleApplication.deleteAssetsFromLocalStorage();

        UiDeviceProvider.getInstance().pressHome();
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
}
