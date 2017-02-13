package com.invisibleteam.goinvisible;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PermissionTests {

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
    @Test
    public void whenAccessIsDeniedThenShowMissingPermissionsDialog() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.invisibleteam.goinvisible", appContext.getPackageName());
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
     * - go to permission dialog
     * - select "Don't ask again" option
     * - deny access for GoInvisible app
     * - ensure "Missing permissions" dialog is visible
     * - exit from application
     * - ensure applications was closed
     *
     * @throws Exception
     */
    @Test
    public void whenAccessIsDeniedPermanentlyThenShowMissingPermissionsDialog() throws Exception {


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

    }
}
