package com.journeyOS.plugins.settings;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.journeyOS.core.api.plugins.ISettingsProvider;
import com.journeyOS.literouter.annotation.ARouterInject;

@ARouterInject(api = ISettingsProvider.class)
public class SettingsProviderImpl implements ISettingsProvider {
    @Override
    public void onCreate() {

    }

    @Override
    public Fragment provideSettingsFragment(Activity activity) {
        return SettingsFragment.newInstance(activity);
    }
}
