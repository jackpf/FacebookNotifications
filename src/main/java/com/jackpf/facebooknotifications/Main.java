package com.jackpf.facebooknotifications;

public class Main
{
    public static void main(String[] args)
    {
        try {
            Notification notification = new Notification();
            notification.createSystemTrayIcon();

            NotificationListener notificationListener = new NotificationListener();
            notificationListener.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
