package com.journeyOS.core.api.thread;

import com.journeyOS.core.thread.CoreExecutors;
import com.journeyOS.literouter.annotation.ARouterInject;

import java.util.concurrent.Executor;

@ARouterInject(api = ICoreExecutorsApi.class)
public class CoreExecutorsImpl implements ICoreExecutorsApi {
    CoreExecutors mCoreExecutors;

    @Override
    public void onCreate() {
        mCoreExecutors = new CoreExecutors();
    }

    @Override
    public Executor diskIOThread() {
        return mCoreExecutors.diskIO();
    }

    @Override
    public Executor networkIOThread() {
        return mCoreExecutors.networkIO();
    }

    @Override
    public Executor mainThread() {
        return mCoreExecutors.mainThread();
    }

}
