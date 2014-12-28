package com.jackpf.facebooknotifications.Facebook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Notifications extends Observable implements Iterable
{
    private List<Notification> notifications = new ArrayList<Notification>();

    public void add(Notification notification)
    {
        notifications.add(notification);

        setChanged();
        notifyObservers(notification);
    }

    public void remove(Notification notification)
    {
        notifications.remove(notification);

        setChanged();
        notifyObservers(null);
    }

    public Iterator<Notification> iterator()
    {
        return notifications.iterator();
    }

    public int size()
    {
        return notifications.size();
    }
}
