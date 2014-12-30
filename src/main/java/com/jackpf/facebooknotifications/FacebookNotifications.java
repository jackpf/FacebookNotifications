package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Authenticator;
import com.jackpf.facebooknotifications.Facebook.NotificationClient;
import com.jackpf.facebooknotifications.Facebook.Notifications;

public class FacebookNotifications
{
    public static void main(String[] args)
    {
        try {
            // Listen locally for incoming OAuth callbacks
            new LocalServer().listen();

            Notifications notifications = new Notifications();

            // Initialise system tray icon
            NotificationManager notificationManager = new NotificationManager(notifications);
            notificationManager.createSystemTrayIcon();

            // Initialise Facebook client
            final NotificationClient notificationClient = new NotificationClient(
                notifications,
                new Authenticator("630238967098572", "436975f7333c19b5ef2d4e589c470a81")
            );

            // Periodically check for notifications!
            notificationClient.start(0, 60);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
