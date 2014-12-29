package com.jackpf.facebooknotifications.Facebook;

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

    public void add(Notification notification)
    {
        notifications.add(notification);

        Collections.sort(notifications, new Comparator<Notification>() {
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
        notifyObservers(notification);
    }

    public void remove(Notification notification)
    {
        notifications.remove(notification);

        setChanged();
        notifyObservers(notification);
    }

    public Iterator<Notification> iterator()
    {
        return notifications.iterator();
    }

    public int size()
    {
        return notifications.size();
    }

    public boolean contains(Notification notification)
    {
        for (Notification n : notifications) {
            if (n.id.equals(notification.id)) {
                return true;
            }
        }

        return false;
    }
}
