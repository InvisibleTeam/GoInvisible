package com.invisibleteam.goinvisible.tests;

import android.support.test.runner.AndroidJUnit4;

import com.invisibleteam.goinvisible.pages.GoInvisibleApplication;
import com.invisibleteam.goinvisible.pages.ImagesView;
import com.invisibleteam.goinvisible.pages.MissingPermissionsDialog;
import com.invisibleteam.goinvisible.pages.system.SystemPermissionDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class PermissionTests {

    @Before
    public void setUp() throws Exception {
        GoInvisibleApplication.launchFromHomeScreen();
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
     * - ensure GoInvisible ListView is opened
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

        assertTrue(ImagesView.isOpened());
    }
}
