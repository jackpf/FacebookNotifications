package com.jackpf.facebooknotifications;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadScheduler
{
    public static void run(final Runnable runnable, int intialDelay, int delay)
    {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            private Future<?> lastExecution;

            @Override
            public void run() {
                if (lastExecution != null && !lastExecution.isDone()) {
                    return;
                }
                lastExecution = executor.submit(runnable);
            }
        }, intialDelay, delay, TimeUnit.SECONDS);
    }
}
