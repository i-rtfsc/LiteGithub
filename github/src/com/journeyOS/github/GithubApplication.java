package com.journeyOS.github;

import android.app.Application;

import com.journeyOS.core.CoreManager;

public class GithubApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CoreManager.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
