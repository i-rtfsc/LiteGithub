package com.journeyOS.core.api.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.journeyOS.core.api.ICoreApi;

public interface IRepositoryProvider extends ICoreApi {
    void navigationRepositoryActivity(@NonNull Context context, @NonNull String owner,
                                      @NonNull String repoName);
}
