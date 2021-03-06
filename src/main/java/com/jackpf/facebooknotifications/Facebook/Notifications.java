package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Connection;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Notifications extends Observable implements Iterable
{
    private List<Notification> notifications = new ArrayList<Notification>();

    public class Event
    {
        public final Notification notification;
        public final int operation;
        public static final int ADD = 1, REMOVE = 2, SET = 3;

        public Event(Notification notification, int operation)
        {
            this.notification = notification;
            this.operation = operation;
        }
    }

    public void set(Connection<Notification> notifications)
    {
        setChanged();
        notifyObservers(new Event(null, Event.SET));

        for (Notification notification : this.notifications) {
            if (!containsNotification(notification, notifications.getData())) {
                this.notifications.remove(notification);
            }
        }

        for (Notification notification : notifications.getData()) {
            if (contains(notification)) {
                return;
            }

            this.notifications.add(notification);

            Collections.sort(this.notifications, new Comparator<Notification>() {
                @Override
                public int compare(Notification o1, Notification o2) {
                    try {
                        return o1.getParsedDate().before(o2.getParsedDate()) ? 1 : -1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }
            });

            setChanged();
            notifyObservers(new Event(notification, Event.ADD));
        }
    }

    public void remove(Notification notification)
    {
        notifications.remove(notification);

        setChanged();
        notifyObservers(new Event(notification, Event.REMOVE));
    }

    public Notification get(int index)
    {
        return notifications.get(index);
    }

    public Iterator<Notification> iterator()
    {
        return notifications.iterator();
    }

    public int size()
    {
        return notifications.size();
    }

    private boolean containsNotification(Notification notification, Iterable notifications)
    {
        for (Object o : notifications) {
            Notification n = (Notification) o;

            if (n.id.equals(notification.id)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(Notification notification)
    {
        return containsNotification(notification, this);
    }
}
