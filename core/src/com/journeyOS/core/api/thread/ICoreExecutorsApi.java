package com.journeyOS.core.api.thread;

import android.os.Handler;

import com.journeyOS.core.api.ICoreApi;

import java.util.concurrent.Executor;


public interface ICoreExecutorsApi extends ICoreApi {

    Executor diskIOThread();

    Executor networkIOThread();

    Handler mainThread();
}
