package com.invisibleteam.goinvisible.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.invisibleteam.goinvisible.model.ClearingInterval;

public class SharedPreferencesUtil {

    private static final String TAG = SharedPreferencesUtil.class.getSimpleName();
    private static final String PREFERENCES_CLEARING_SERVICE_INTERVAL = "clearing_service_interval";
    private static final String PREFERENCES_CLEARING_SERVICE_ENABLED = "clearing_service_on";

    public static void saveInterval(Context context, ClearingInterval interval) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFERENCES_CLEARING_SERVICE_INTERVAL, interval.name());
        editor.apply();
    }

    public static void setClearingServiceActivation(Context context, boolean isEnabled) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFERENCES_CLEARING_SERVICE_ENABLED, isEnabled);
        editor.apply();
    }

    public static boolean isClearingServiceActivated(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            return sharedPrefs.getBoolean(PREFERENCES_CLEARING_SERVICE_ENABLED, false);
        } catch (Exception e) {
            Log.e(TAG, "Error while get interval from preferences:", e);
            //TODO Crashlitycs log
            return false;
        }
    }

    public static ClearingInterval getInterval(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            String interval = sharedPrefs.getString(PREFERENCES_CLEARING_SERVICE_INTERVAL, null);
            return ClearingInterval.valueOf(interval);
        } catch (Exception e) {
            Log.e(TAG, "Error while get interval from preferences:", e);
            //TODO Crashlitycs log
            return ClearingInterval.DAY;
        }
    }
}