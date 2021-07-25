package com.journeyOS.core.api.plugins;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.journeyOS.core.api.ICoreApi;

public interface IAboutProvider extends ICoreApi {
    Fragment provideAboutFragment(Activity activity);
}
