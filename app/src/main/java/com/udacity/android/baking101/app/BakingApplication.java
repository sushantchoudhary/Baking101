package com.udacity.android.baking101.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class BakingApplication extends Application {
    private static BakingApplication bakingApplication;
    private static Context context;

    public static BakingApplication getMyApplication() {
        return bakingApplication;
    }

    public static Context getAppContext() {
        return BakingApplication.context;
    }

    public void onCreate() {
        super.onCreate();

        bakingApplication = this;
        BakingApplication.context = getApplicationContext();

        initializeStetho();

    }

    public void initializeStetho() {
        Stetho.initializeWithDefaults(context);
    }
}
