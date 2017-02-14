package com.invisibleteam.goinvisible.tests;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.Config;
import com.invisibleteam.goinvisible.UiDeviceProvider;
import com.invisibleteam.goinvisible.pages.FileView;
import com.invisibleteam.goinvisible.pages.MissingPermissionsDialog;
import com.invisibleteam.goinvisible.pages.system.SystemPermissionDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
//TODO clean permissions for GoInvisible app between tests in setup or teardown

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PermissionTests {

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(Config.GOINVISIBLE_PACKAGE, appContext.getPackageName());

        // Initialize UiDevice instance
        mDevice = UiDeviceProvider.getInstance();

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), Config.LAUNCH_TIMEOUT);

        // TODO: 14.02.2017 move stuff under to GoInvisible as launch() method
        // Launch the GoInvisible app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(Config.GOINVISIBLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(Config.GOINVISIBLE_PACKAGE).depth(0)), Config.LAUNCH_TIMEOUT);
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
     * - exit from application
     * - ensure applications was closed
     *
     * @throws Exception
     */
    @Test
    public void whenAccessIsDeniedThenShowMissingPermissionsDialog() throws Exception {
        // Ensure correct permission dialog is opened
        SystemPermissionDialog.isOpened();

        // Deny access for GoInvisible app
        SystemPermissionDialog.denyAccess();

        // Ensure "Missing permissions" dialog is opened
        MissingPermissionsDialog.isOpened();

        // Exit from application
        MissingPermissionsDialog.exitApplication();

        // Ensure applications was closed
        mDevice.wait(Until.hasObject(By.pkg(mDevice.getLauncherPackageName()).depth(0)), Config.LAUNCH_TIMEOUT);
        String pidOfGoInvisible = mDevice.executeShellCommand("pidof " + Config.GOINVISIBLE_PACKAGE);
        assertEquals("\n", pidOfGoInvisible);
    }

    /**
     * Test case:
     * - Show missing permissions dialog when "Do not ask" was checked
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
     * - select "Don't ask again" option
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is opened
     * - exit from application
     * - ensure applications was closed
     *
     * @throws Exception
     */
    @Test
    public void whenAccessIsDeniedPermanentlyThenShowMissingPermissionsDialog() throws Exception {
        // Ensure correct permission dialog is opened
        SystemPermissionDialog.isOpened();

        // Deny access for GoInvisible app
        SystemPermissionDialog.denyAccess();

        // Ensure "Missing permissions" dialog is opened
        MissingPermissionsDialog.isOpened();

        // Go to permission dialog again
        MissingPermissionsDialog.ok();

        // Select "Don't ask again" option
        SystemPermissionDialog.doNotAskAgain(true);

        // Deny access for GoInvisible app
        SystemPermissionDialog.denyAccess();

        // Ensure "Missing permissions" dialog is opened
        MissingPermissionsDialog.isOpened();

        // Exit from application
        MissingPermissionsDialog.exitApplication();

        // Ensure applications was closed
        mDevice.wait(Until.hasObject(By.pkg(mDevice.getLauncherPackageName()).depth(0)), Config.LAUNCH_TIMEOUT);
        String pidOfGoInvisible = mDevice.executeShellCommand("pidof " + Config.GOINVISIBLE_PACKAGE);
        assertEquals("\n", pidOfGoInvisible);
    }

    /**
     * Test case:
     * - Show file view when permission is allowed
     * <p>
     * Preconditions:
     * - GoInvisible app is clean installed
     * - GoInvisible app is opened first time
     * - permission dialog should be opened
     * <p>
     * Steps to execute:
     * - ensure correct permission dialog is opened (GoInvisible app requests access to files)
     * - allow access for GoInvisible app
     * - ensure GoInvisible file view is opened
     *
     * @throws Exception
     */
    @Test
    public void whenAccessToPhotosIsEnabledThenShowMainView() throws Exception {
        // Ensure correct permission dialog is opened
        SystemPermissionDialog.isOpened();

        // Allow access for GoInvisible app
        SystemPermissionDialog.allowAccess();

        // Ensure GoInvisible file view is opened
        FileView.isOpened();
    }
}
