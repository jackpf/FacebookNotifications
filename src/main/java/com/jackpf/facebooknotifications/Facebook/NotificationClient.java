package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

import java.net.URL;

import javax.imageio.ImageIO;

public class NotificationClient extends DefaultFacebookClient
{
    private Notifications notifications;
    private Authenticator authenticator;

    public NotificationClient(Notifications notifications, Authenticator authenticator)
    {
        super("CAAI9MvHB7MwBAOOW1qh4j52ZC3x73PlwfZA3d1m80Ts2FjjLyEM9brEMQU7qYDiBrK4RzwsySkCDPpsVoMPAl9RuYZAD6HdYFF7GXS7GOUmx2NXTaBVUkzNBldkNoaoNWWYWqulTsF0YJXfByWxvkZArnVuJlWEFY0uKHAWYv3iX7kXZC78njovmartlHt1ZAGhx0LNp9ukiekgfbYMPAm");//authenticator.getAccessToken());
        this.notifications = notifications;
    }

    private void addIcon(Notification notification)
    {
        User user = fetchObject(notification.from.id, User.class, Parameter.with("fields", "picture"), Parameter.with("type", "large"));

        try {
            notification.image = ImageIO.read(new URL(user.getPicture().getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNotifications()
    {
        try {
            Connection<Notification> notifications = fetchConnection("me/notifications", Notification.class);

            int i = 0;
            for (Notification notification : notifications.getData()) {
                // Extras
                addIcon(notification);
                notification.position = notifications.getData().size() - i;
                i++;

                this.notifications.add(notification);
            }
        } catch (FacebookOAuthException e) {
            System.err.println(e.getMessage());
        }
    }
}
