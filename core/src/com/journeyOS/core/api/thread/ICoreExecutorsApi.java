package com.journeyOS.core.api.thread;

import com.journeyOS.core.api.ICoreApi;

import java.util.concurrent.Executor;

public interface ICoreExecutorsApi extends ICoreApi {
    Executor diskIOThread();

    Executor networkIOThread();

    Executor mainThread();
}
