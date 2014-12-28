package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Authenticator;
import com.jackpf.facebooknotifications.Facebook.NotificationClient;
import com.jackpf.facebooknotifications.Facebook.Notifications;

public class Main
{
    public static void main(String[] args)
    {
        try {
            Notifications notifications = new Notifications();

            final NotificationManager notificationManager = new NotificationManager(notifications);
            notificationManager.createSystemTrayIcon();

            final NotificationClient notificationClient = new NotificationClient(
                notifications,
                new Authenticator("630238967098572", "436975f7333c19b5ef2d4e589c470a81")
            );

            notificationClient.getNotifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
