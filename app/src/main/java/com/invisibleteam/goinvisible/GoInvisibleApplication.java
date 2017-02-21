package com.invisibleteam.goinvisible;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.model.ClearingInterval;
import com.invisibleteam.goinvisible.service.ClearingTagsReceiver;
import com.invisibleteam.goinvisible.service.ClearingTagsService;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;

public class GoInvisibleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            setupStrictMode();
            setupLeakCanary();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected void setupStrictMode() {
        // https://medium.com/@elye.project/walk-through-hell-with-android-strictmode-7e8605168032
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                .detectFileUriExposure()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vmPolicyBuilder.detectCleartextNetwork();
        }
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static void startClearingServiceAlarm(Context context) {
        ClearingInterval clearingInterval = SharedPreferencesUtil.getInterval(context);
        if (clearingInterval == null) {
            return;
        }

        Intent alarmIntent = new Intent(context, ClearingTagsReceiver.class);
        alarmIntent.putExtra(ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE_KEY,
                ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                clearingInterval.getInterval(),
                pendingIntent);
    }

    public static void stopClearingServiceAlarm(Context context) {
        Intent alarmIntent = new Intent(context, ClearingTagsReceiver.class);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        Intent intent = new Intent(context, ClearingTagsService.class);
        context.stopService(intent);
    }
}
