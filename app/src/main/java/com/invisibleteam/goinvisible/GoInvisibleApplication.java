package com.invisibleteam.goinvisible;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.VisibleForTesting;

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

}
