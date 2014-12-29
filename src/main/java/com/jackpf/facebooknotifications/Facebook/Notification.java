package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Facebook;

import org.ocpsoft.prettytime.PrettyTime;

import java.awt.Image;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification
{
    public static class User
    {
        @Facebook
        public String id;

        @Facebook
        public String name;
    }

    public static class Application
    {
        @Facebook
        public String id;

        @Facebook
        public String name;

        @Facebook
        public String namespace;
    }

    @Facebook
    public String id;

    @Facebook
    public User from;

    @Facebook
    public User to;

    @Facebook("created_time")
    public String createdTime;

    @Facebook("updated_time")
    public String updatedTime;

    @Facebook
    public String title;

    @Facebook
    public String link;

    @Facebook
    public Application application;

    @Facebook
    public int unread;

    public Image image;

    // Object?

    public String getTitle(int len)
    {
        if (title.length() > len) {
            return title.substring(0, len) + "...";
        } else {
            return title;
        }
    }

    public Date getParsedDate() throws ParseException
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZZZZ", Locale.getDefault());
        return df.parse(updatedTime);
    }

    public String getPrettyDate()
    {
        try {
            return new PrettyTime().format(getParsedDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}