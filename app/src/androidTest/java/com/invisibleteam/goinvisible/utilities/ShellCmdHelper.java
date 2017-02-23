package com.invisibleteam.goinvisible.utilities;

import android.util.Log;

import java.io.IOException;

import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;

public class ShellCmdHelper {

    private final static String PM_GRANT_CMD = "pm grant %s %s";
    private static final String AM_START_CAMERA = "am start -W -N com.android.camera";

    /**
     * @param packageName package that will get permission
     * @param permission  Manifest.permission string
     * @return the standard output of the command
     */
    public static String grantPermission(String packageName, String permission) {
        String formattedShellCommand = formatShellCommand(PM_GRANT_CMD, packageName, permission);
        return executeShellCommand(formattedShellCommand);
    }
    
    public static String startCamera() {
        return executeShellCommand(AM_START_CAMERA);
    }

    private static String executeShellCommand(String shellCommand) {
        String result = null;

        try {
            result = UI_DEVICE.executeShellCommand(shellCommand);
        } catch (IOException e) {
            Log.e("ShellCmdHelper", "Failed to executeShellCommand", e);
        }

        return result;
    }

    private static String formatShellCommand(String command, String... arguments) {
        return String.format(command, arguments);
    }
}
