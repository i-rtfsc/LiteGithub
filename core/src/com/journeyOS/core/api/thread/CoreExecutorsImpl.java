package com.journeyOS.core.api.thread;

import android.os.Handler;

import com.journeyOS.literouter.BaseUtils;
import com.journeyOS.literouter.annotation.ARouterInject;
import com.journeyOS.litetask.TaskScheduler;

import java.util.concurrent.Executor;


@ARouterInject(api = ICoreExecutorsApi.class)
public class CoreExecutorsImpl implements ICoreExecutorsApi {
    private static final int THREAD_COUNT = 3;
    private static final String DISK_THREAD = "disk_thread";
    private static final String NETWORK_THREAD = "network_thread";

    @Override
    public void onCreate() {
        TaskScheduler.getInstance().provideSingleThread(DISK_THREAD);
        TaskScheduler.getInstance().provideFixedThread(NETWORK_THREAD, THREAD_COUNT);
    }

    @Override
    public Executor diskIOThread() {
        Executor disk = TaskScheduler.getInstance().getExecutor(DISK_THREAD);
        if (BaseUtils.isNull(disk)) {
            disk = TaskScheduler.getInstance().provideSingleThread(DISK_THREAD);
        }

        return disk;
    }

    @Override
    public Executor networkIOThread() {
        Executor network = TaskScheduler.getInstance().getExecutor(NETWORK_THREAD);
        if (BaseUtils.isNull(network)) {
            network = TaskScheduler.getInstance().provideFixedThread(NETWORK_THREAD, THREAD_COUNT);
        }

        return network;
    }

    @Override
    public Handler mainThread() {
        return TaskScheduler.getInstance().getMainHandler();
    }

}
