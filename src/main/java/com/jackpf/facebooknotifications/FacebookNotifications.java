package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Authenticator;
import com.jackpf.facebooknotifications.Facebook.NotificationClient;
import com.jackpf.facebooknotifications.Facebook.Notifications;

import javax.swing.JOptionPane;

public class FacebookNotifications
{
    public static void main(String[] args)
    {
        try {
            // Listen locally for incoming OAuth callbacks
            new LocalServer().listen();

            Notifications notifications = new Notifications();

            // Initialise system tray icon
            final NotificationManager notificationManager = new NotificationManager(notifications);
            notificationManager.createSystemTrayIcon();

            // Initialise Facebook client
            KeyManager keyManager = new KeyManager();
            keyManager.loadKeys();

            final NotificationClient notificationClient = new NotificationClient(
                notifications,
                new Authenticator(keyManager.getKey("api_key"), keyManager.getKey("api_secret"))
            );

            // Periodically check for notifications!
            notificationClient.start(0, 60, new NotificationClient.Callback()
            {
                @Override
                public void update(Notifications notifications, Exception e)
                {
                    // Need a better way of letting the notification know we've authenticated...
                    if (e == null && notifications.size() == 0) {
                        notificationManager.setMessage("NO UPDATES");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                null,
                e.getClass().getName() + ": " + e.getMessage(),
                "An error has occurred",
                JOptionPane.ERROR_MESSAGE
            );

            System.exit(-1);
        }
    }
}
