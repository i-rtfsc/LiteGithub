package com.journeyOS.github.ui.activity.viewer;

import android.content.Context;
import androidx.annotation.NonNull;

import com.journeyOS.core.api.repository.IRepositoryProvider;
import com.journeyOS.literouter.annotation.ARouterInject;

@ARouterInject(api = IRepositoryProvider.class)
public class RepositoryProviderImpl implements IRepositoryProvider {
    @Override
    public void onCreate() {

    }

    @Override
    public void navigationRepositoryActivity(@NonNull Context context, @NonNull String owner, @NonNull String repoName) {
        RepositoryActivity.show(context, owner, repoName);
    }
}
