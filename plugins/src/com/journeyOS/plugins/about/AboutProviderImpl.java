package com.journeyOS.plugins.about;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.journeyOS.core.api.plugins.IAboutProvider;
import com.journeyOS.literouter.annotation.ARouterInject;

@ARouterInject(api = IAboutProvider.class)
public class AboutProviderImpl implements IAboutProvider {
    @Override
    public void onCreate() {

    }

    @Override
    public Fragment provideAboutFragment(Activity activity) {
        return AboutFragment.newInstance(activity);
    }
}
