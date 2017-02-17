package com.invisibleteam.goinvisible.tests;

import android.support.test.runner.AndroidJUnit4;

import com.invisibleteam.goinvisible.pages.GoInvisiblePackage;
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
        GoInvisiblePackage.grantStoragePermissions();

        GoInvisiblePackage.copyAssetsToLocalStorage();

        GoInvisiblePackage.launchFromHomeScreen();
    }

    @After
    public void tearDown() throws Exception {
        UiDeviceProvider.getInstance().pressHome();
    }

    @Test
    public void checkImagesAreListed() throws Exception {
        assertTrue(ImagesView.isOpened());
        assertTrue(ImagesView.isAnyImageLoaded());
    }
}
