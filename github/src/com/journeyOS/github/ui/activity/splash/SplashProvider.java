package com.journeyOS.github.ui.activity.splash;

import android.content.Context;

import com.journeyOS.core.api.splash.ISplashProvider;
import com.journeyOS.literouter.annotation.ARouterInject;

@ARouterInject(api = ISplashProvider.class)
public class SplashProvider implements ISplashProvider {

    @Override
    public void navigationSplashActivity(Context context) {
        SplashActivity.newInstance(context);
    }

    @Override
    public void onCreate() {

    }
}
