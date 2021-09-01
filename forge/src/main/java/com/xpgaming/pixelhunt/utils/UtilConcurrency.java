package com.xpgaming.pixelhunt.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UtilConcurrency {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    public static void runAsync(Runnable runnable) {
        EXECUTOR_SERVICE.submit(runnable);
    }

}
