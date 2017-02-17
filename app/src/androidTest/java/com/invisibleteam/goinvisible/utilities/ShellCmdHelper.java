package com.invisibleteam.goinvisible.utilities;

import java.io.IOException;

public class ShellCmdHelper {

    private final static String PM_GRANT_CMD = "pm grant %s %s";

    /**
     * @param packageName package that will get permission
     * @param permission  Manifest.permission string
     * @return the standard output of the command
     */
    public static String grantPermission(String packageName, String permission) {
        String formattedShellCommand = formatShellCommand(PM_GRANT_CMD, packageName, permission);
        return executeShellCommand(formattedShellCommand);
    }

    private static String executeShellCommand(String shellCommand) {
        String result = null;

        try {
            result = UiDeviceProvider.getInstance().executeShellCommand(shellCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String formatShellCommand(String command, String... arguments) {
        return String.format(command, arguments);
    }
}
