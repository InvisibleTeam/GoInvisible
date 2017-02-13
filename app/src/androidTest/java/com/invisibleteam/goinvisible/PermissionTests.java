package com.invisibleteam.goinvisible;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
//TODO clean permissions for GoInvisible app between tests in setup or teardown
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PermissionTests {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String GOINVISIBLE_PACKAGE = "com.invisibleteam.goinvisible";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;

    // Permission Dialog selectors
    private final BySelector permissionMessageSelector = By.text("Allow GoInvisible to access photos, media, and files on your device?");
    private final BySelector dontAskAgainSelector = By.text("Don't ask again");
    private final BySelector denyButtonTextSelector = By.text("DENY");
    private final BySelector allowButtonTextSelector = By.text("ALLOW");

    // Missing permissions dialog selectors
    private final BySelector missingPermissionsMessageSelector = By.text("Storage permission is necessary to use this application.");
    private final BySelector missingPermissionsTitleSelector = By.text("Missing permissions");
    private final BySelector exitButtonSelector = By.text("EXIT APPLICATION");
    private final BySelector okButtonSelector = By.text("OK");

    @Before
    public void startMainActivityFromHomeScreen() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(GOINVISIBLE_PACKAGE, appContext.getPackageName());

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the GoInvisible app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(GOINVISIBLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(GOINVISIBLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    /**
     * Test case:
     * - Show missing permissions dialog
     *
     * Preconditions:
     * - GoInvisible app is clean installed
     * - GoInvisible app is opened first time
     * - permission dialog should be visible
     *
     * Steps to execute:
     * - ensure correct permission dialog is opened (GoInvisible app requests access to files)
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is visible
     * - exit from application
     * - ensure applications was closed
     *
     * @throws Exception
     */
    @Ignore("on System.exit(0) in application test will crash. Waiting for problem resolution by dev.")
    @Test
    public void whenAccessIsDeniedThenShowMissingPermissionsDialog() throws Exception {
        // Ensure correct permission dialog is opened
        mDevice.wait(Until.hasObject(permissionMessageSelector), LAUNCH_TIMEOUT);

        // Deny access for GoInvisible app
        mDevice.wait(Until.hasObject(denyButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(denyButtonTextSelector).click();

        // Ensure "Missing permissions" dialog is visible
        mDevice.wait(Until.hasObject(missingPermissionsTitleSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(missingPermissionsMessageSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(exitButtonSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(okButtonSelector), LAUNCH_TIMEOUT);

        // Exit from application
        mDevice.findObject(exitButtonSelector).click();

        // Ensure applications was closed
        mDevice.wait(Until.hasObject(By.pkg(mDevice.getLauncherPackageName()).depth(0)), LAUNCH_TIMEOUT);
        String pidOfGoInvisible = mDevice.executeShellCommand("pidof " + GOINVISIBLE_PACKAGE);
        assertEquals("\n", pidOfGoInvisible);
    }

    /**
     * Test case:
     * - Show missing permissions dialog when "Do not ask" was checked
     *
     * Preconditions:
     * - GoInvisible app is clean installed
     * - GoInvisible app is opened first time
     * - permission dialog should be visible
     *
     * Steps to execute:
     * - ensure correct permission dialog is opened (GoInvisible app requests access to files)
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is visible
     * - go to permission dialog again
     * - select "Don't ask again" option
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is visible
     * - exit from application
     * - ensure applications was closed
     *
     * @throws Exception
     */
    @Ignore("on System.exit(0) in application test will crash. Waiting for problem resolution by dev.")
    @Test
    public void whenAccessIsDeniedPermanentlyThenShowMissingPermissionsDialog() throws Exception {
        // Ensure correct permission dialog is opened
        mDevice.wait(Until.hasObject(permissionMessageSelector), LAUNCH_TIMEOUT);

        // Deny access for GoInvisible app
        mDevice.wait(Until.hasObject(denyButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(denyButtonTextSelector).click();

        // Ensure "Missing permissions" dialog is visible
        mDevice.wait(Until.hasObject(missingPermissionsTitleSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(missingPermissionsMessageSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(exitButtonSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(okButtonSelector), LAUNCH_TIMEOUT);

        // Go to permission dialog again
        mDevice.findObject(okButtonSelector).click();

        // Select "Don't ask again" option
        mDevice.wait(Until.hasObject(dontAskAgainSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(dontAskAgainSelector).click();

        // Deny access for GoInvisible app
        mDevice.wait(Until.hasObject(denyButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(denyButtonTextSelector).click();

        // Ensure "Missing permissions" dialog is visible
        mDevice.wait(Until.hasObject(missingPermissionsTitleSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(missingPermissionsMessageSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(exitButtonSelector), LAUNCH_TIMEOUT);
        mDevice.wait(Until.hasObject(okButtonSelector), LAUNCH_TIMEOUT);

        // Exit from application
        mDevice.findObject(exitButtonSelector).click();

        // Ensure applications was closed
        mDevice.wait(Until.hasObject(By.pkg(mDevice.getLauncherPackageName()).depth(0)), LAUNCH_TIMEOUT);
        String pidOfGoInvisible = mDevice.executeShellCommand("pidof " + GOINVISIBLE_PACKAGE);
        assertEquals("\n", pidOfGoInvisible);
    }

    /**
     * Test case:
     * - Show file view when permission is allowed
     *
     * Preconditions:
     * - GoInvisible app is clean installed
     * - GoInvisible app is opened first time
     * - permission dialog should be visible
     *
     * Steps to execute:
     * - ensure correct permission dialog is opened (GoInvisible app requests access to files)
     * - allow access for GoInvisible app
     * - ensure GoInvisible file view is visible
     *
     * @throws Exception
     */
    @Test
    public void whenAccessToPhotosIsEnabledThenShowMainView() throws Exception {
        // Ensure correct permission dialog is opened
        mDevice.wait(Until.hasObject(permissionMessageSelector), LAUNCH_TIMEOUT);

        // Allow access for GoInvisible app
        mDevice.wait(Until.hasObject(allowButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(allowButtonTextSelector).click();

        // Ensure GoInvisible file view is visible
        // TODO add check that GoInvisible screen is visible
        String pidOfGoInvisible = mDevice.executeShellCommand("pidof " + GOINVISIBLE_PACKAGE);
        assertNotEquals("\n", pidOfGoInvisible);
    }
}
