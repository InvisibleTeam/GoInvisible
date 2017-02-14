package com.invisibleteam.goinvisible.tests;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.Config;
import com.invisibleteam.goinvisible.UiDeviceProvider;
import com.invisibleteam.goinvisible.pages.FileView;
import com.invisibleteam.goinvisible.pages.GoInvisiblePackage;
import com.invisibleteam.goinvisible.pages.MissingPermissionsDialog;
import com.invisibleteam.goinvisible.pages.system.SystemHomeView;
import com.invisibleteam.goinvisible.pages.system.SystemPermissionDialog;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Permission tests must be executed in order:
 * - deny permission test
 * - allow permission test
 * <p>
 * The reason is that after revoking permission application is killed.
 * Such behaviour leads to failed test as test runner does not expect it.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PermissionTests {

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDeviceProvider.getInstance();

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        assertTrue(SystemHomeView.isOpened());

        // TODO: 14.02.2017 move stuff under to GoInvisible as launch() method
        // Launch the GoInvisible app
        GoInvisiblePackage.launch();

        // Wait for the app to appear
        assertTrue(GoInvisiblePackage.isOpened());
    }

    /**
     * Test case:
     * - Show missing permissions dialog
     * <p>
     * Preconditions:
     * - GoInvisible app is clean installed
     * - GoInvisible app is opened first time
     * - permission dialog should be opened
     * <p>
     * Steps to execute:
     * - ensure correct permission dialog is opened (GoInvisible app requests access to files)
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is opened
     * - go to permission dialog again
     * - allow access for GoInvisible app
     * - ensure GoInvisible file view is opened
     *
     * @throws Exception
     */
    @Test
    public void checkPermissionFlow() throws Exception {
        assertTrue(SystemPermissionDialog.isOpened());
        SystemPermissionDialog.denyAccess();

        assertTrue(MissingPermissionsDialog.isOpened());
        MissingPermissionsDialog.ok();

        SystemPermissionDialog.allowAccess();

        assertTrue(FileView.isOpened());
    }
}
