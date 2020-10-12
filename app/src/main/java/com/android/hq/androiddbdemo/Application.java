package com.android.hq.androiddbdemo;

import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
