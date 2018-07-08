package com.journeyOS.core.thread;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.journeyOS.litetask.TaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class CoreExecutors {
    private static final int THREAD_COUNT = 5;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    CoreExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public CoreExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

//    每个数据库都创建线程太浪费资源了。
//    /**
//     * 每个数据库都是创建一个自己的线程来查询，这样不同数据库之前操作就不需要等待了。
//     * 无需用diskIO
//     *
//     */
//    public static class DatabaseThreadExecutor implements Executor {
//        String handlerName;
//
//        public DatabaseThreadExecutor(String handlerName) {
//            this.handlerName = handlerName;
//        }
//
//        private final Handler databaseThreadHandler = TaskScheduler.provideHandler(handlerName);
//
//        @Override
//        public void execute(@NonNull Runnable command) {
//            databaseThreadHandler.post(command);
//        }
//    }
}
