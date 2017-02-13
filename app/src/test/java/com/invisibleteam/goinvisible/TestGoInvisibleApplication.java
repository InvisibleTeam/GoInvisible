package com.invisibleteam.goinvisible;


import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

public class TestGoInvisibleApplication extends GoInvisibleApplication implements TestLifecycleApplication {

    @Override
    protected void setupStrictMode() {

    }

    @Override
    protected void setupLeakCanary() {

    }

    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {
    }

    @Override
    public void afterTest(Method method) {

    }
}
