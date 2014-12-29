package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Authenticator;
import com.jackpf.facebooknotifications.Facebook.NotificationClient;
import com.jackpf.facebooknotifications.Facebook.Notifications;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args)
    {
        try {
            new LocalServer().listen();

            Notifications notifications = new Notifications();

            final NotificationManager notificationManager = new NotificationManager(notifications);
            notificationManager.createSystemTrayIcon();

            final NotificationClient notificationClient = new NotificationClient(
                notifications,
                new Authenticator("630238967098572", "436975f7333c19b5ef2d4e589c470a81")
            );

            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    notificationClient.getNotifications();
                }
            };

            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
                private final ExecutorService executor = Executors.newSingleThreadExecutor();
                private Future<?> lastExecution;

                @Override
                public void run() {
                    if (lastExecution != null && !lastExecution.isDone()) {
                        return;
                    }
                    lastExecution = executor.submit(r);
                    System.err.println("updating");
                }
            }, 0, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
