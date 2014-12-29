package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

public class NotificationClient extends DefaultFacebookClient implements Observer
{
    private Notifications notifications;
    private Authenticator authenticator;

    public NotificationClient(Notifications notifications, Authenticator authenticator)
    {
        super(authenticator.getAccessToken());
        this.notifications = notifications;
        this.authenticator = authenticator;

        notifications.addObserver(this);
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

                if (!this.notifications.contains(notification)) {
                    this.notifications.add(notification);
                }
            }
        } catch (FacebookOAuthException e) {
            e.printStackTrace();

            this.accessToken = authenticator.getAccessToken(true);
        }
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

    /**
     * Not sure if restfb has a way of marking notifications as read
     * So for now this just manually sends a post request to mark the notification as read
     */
    @Override
    public void update(Observable observable, Object data)
    {
        Notification notification = (Notification) data;

        if (!((Notifications) observable).contains(notification)) {
            HttpClient client = HttpClientBuilder.create().build();

            HttpPost request = new HttpPost(
                String.format(
                    "https://graph.facebook.com/%s?access_token=%s&unread=false",
                    notification.id,
                    authenticator.getAccessToken()
                )
            );

            try {
                client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
