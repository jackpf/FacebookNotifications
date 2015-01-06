package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.exception.FacebookOAuthException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationClient extends DefaultFacebookClient implements Observer, Runnable
{
    private Notifications notifications;
    private Authenticator authenticator;
    private ImageCache imageCache;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private Callback callback;

    public NotificationClient(Notifications notifications, Authenticator authenticator)
    {
        super(authenticator.getAccessToken());
        this.notifications = notifications;
        this.authenticator = authenticator;
        this.imageCache = new ImageCache(authenticator);

        notifications.addObserver(this);
    }

    public interface Callback
    {
        public void update(Notifications notifications, Exception e);
    }

    public void start(int initialDelay, int delay)
    {
        start(initialDelay, delay, null);
    }

    public void start(int initialDelay, int delay, Callback callback)
    {
        this.callback = callback;
        final NotificationClient self = this;

        scheduledExecutorService.scheduleAtFixedRate(new Runnable()
        {
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            private Future<?> lastExecution;

            @Override
            public void run()
            {
                if (lastExecution != null && !lastExecution.isDone()) {
                    return;
                }

                lastExecution = executor.submit(self);
            }
        }, initialDelay, delay, TimeUnit.SECONDS);
    }

    public void stop()
    {
        scheduledExecutorService.shutdown();
    }

    @Override
    public void run()
    {
        try {
            Connection<Notification> notifications = fetchConnection("me/notifications", Notification.class);

            for (Notification notification : notifications.getData()) {
                // Extras
                addIcon(notification);
            }

            this.notifications.set(notifications);

            if (callback != null) {
                callback.update(this.notifications, null);
            }
        } catch (FacebookOAuthException e) {
            e.printStackTrace();

            if (callback != null) {
                callback.update(this.notifications, e);
            }

            //stop();
            this.accessToken = authenticator.getAccessToken(true);
            run();
        }
    }

    private void addIcon(Notification notification)
    {
        /* Apparently User.getPicture() is no longer the correct way to get get user's picture
        User user = fetchObject(notification.from.id, User.class, Parameter.with("fields", "picture")); */

        try {
            notification.image = imageCache.getImage(notification.from.id);
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
        final Notifications.Event event = (Notifications.Event) data;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (event.operation == Notifications.Event.REMOVE) {
                    HttpClient client = HttpClientBuilder.create().build();

                    HttpPost request = new HttpPost(
                        String.format(
                            "https://graph.facebook.com/%s?access_token=%s&unread=false",
                            event.notification.id,
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
        }).start();
    }
}
